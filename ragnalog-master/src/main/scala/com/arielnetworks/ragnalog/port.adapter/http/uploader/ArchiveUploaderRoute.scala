package com.arielnetworks.ragnalog.port.adapter.http.uploader

import java.io.File

import akka.http.scaladsl.model.Multipart
import akka.http.scaladsl.model.Multipart.BodyPart
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.Materializer
import akka.stream.scaladsl.{FileIO, Sink}
import akka.util.ByteString
import com.arielnetworks.ragnalog.application.archive.data.GetContainersResult
import com.arielnetworks.ragnalog.port.adapter.http.route.RouteService
import spray.json.DefaultJsonProtocol._

import scala.concurrent.duration._
import scala.concurrent.{ExecutionContext, Future}
import scalax.file.Path

class ArchiveUploaderRoute extends RouteService {

  implicit val containersFormat = jsonFormat2(GetContainersResult)

  def route(implicit m: Materializer, ec: ExecutionContext): Route =
    pathPrefix("containers" / Segment) { containerId =>
      path("archive" / Segment) { hash =>
        entity(as[Multipart.FormData]) { (formData: Multipart.FormData) =>

          val filePath = Path("/", "tmp", containerId, hash)

          val uploader = ArchiveUploaderFactory.getOrCreate(filePath)

          println(s"uploading: ${filePath.path}")

          // collect all parts of the multipart as it arrives into a map
          val allPartsF: Future[Map[String, Any]] = formData.parts.mapAsync[(String, Any)](1) {

            case body: BodyPart if body.name == "file" =>
              println(s"bodyPart: ${body.name}")
              // stream into a file as the chunks of it arrives and return a future
              // file to where it got stored
              val file = File.createTempFile("upload", "tmp")
              println(file.getAbsolutePath)
              body.entity.dataBytes.runWith(FileIO.toFile(file)).map(_ => body.name -> file)

            case body: BodyPart =>
              println(s"bodyPart: ${body.additionalDispositionParams}, ${body.dispositionParams}")
              // collect form field values
              body.toStrict(2.seconds).map(strict => body.name -> strict.entity.data.utf8String)

          }.runFold(Map.empty[String, Any])((map, tuple) => map + tuple)

          // when processing have finished create a response for the user
          onSuccess(allPartsF) { allParts =>
            println(s"complete: ${allParts}")

            uploader.append(allParts)

            complete {
              "ok!"
            }
          }
        }
      }
    }
}
