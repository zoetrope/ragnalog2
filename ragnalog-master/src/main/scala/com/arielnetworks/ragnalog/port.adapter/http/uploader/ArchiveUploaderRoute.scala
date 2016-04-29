package com.arielnetworks.ragnalog.port.adapter.http.uploader

import java.io.File

import akka.http.scaladsl.model.Multipart
import akka.http.scaladsl.model.Multipart.BodyPart
import akka.http.scaladsl.server.Directives._
import akka.stream.Materializer
import akka.stream.scaladsl.FileIO
import com.arielnetworks.ragnalog.application.archive.data.GetContainersResult
import com.arielnetworks.ragnalog.port.adapter.http.route.RouteService
import spray.json.DefaultJsonProtocol._

import scala.concurrent.duration._
import scala.concurrent.{ExecutionContext, Future}

class ArchiveUploaderRoute extends RouteService {

  implicit val containersFormat = jsonFormat2(GetContainersResult)

  def route(implicit m: Materializer, ec: ExecutionContext) =
    pathPrefix("archive") {
      entity(as[Multipart.FormData]) { (formData: Multipart.FormData) ⇒
        println(s"entity: $formData")

        // collect all parts of the multipart as it arrives into a map
        val allPartsF: Future[Map[String, Any]] = formData.parts.mapAsync[(String, Any)](1) {

          case b: BodyPart if b.name == "file" =>
            println(s"bodyPart: ${b.name}")
            // stream into a file as the chunks of it arrives and return a future
            // file to where it got stored
            val file = File.createTempFile("upload", "tmp")
            println(file.getAbsolutePath)
            b.entity.dataBytes.runWith(FileIO.toFile(file)).map(_ => b.name -> file)

          case b: BodyPart =>
            println("bodyPart")
            // collect form field values
            b.toStrict(2.seconds).map(strict => b.name -> strict.entity.data.utf8String)

        }.runFold(Map.empty[String, Any])((map, tuple) => map + tuple)

        val done = allPartsF.map { allParts =>
          println("done")
          // You would have some better validation/unmarshalling here
        }

        // when processing have finished create a response for the user
        onSuccess(allPartsF) { allParts =>
          println("complete")
          complete {
            "ok!"
          }
        }
      }
    }
}
