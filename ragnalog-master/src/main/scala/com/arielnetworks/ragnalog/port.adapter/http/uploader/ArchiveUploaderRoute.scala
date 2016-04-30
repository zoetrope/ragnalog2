package com.arielnetworks.ragnalog.port.adapter.http.uploader

import java.io.File

import akka.http.scaladsl.model.Multipart
import akka.http.scaladsl.model.Multipart.BodyPart
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.Materializer
import akka.stream.scaladsl.FileIO
import com.arielnetworks.ragnalog.port.adapter.http.route.RouteService

import scala.concurrent.duration._
import scala.concurrent.{ExecutionContext, Future}

class ArchiveUploaderRoute extends RouteService {

  def route(implicit m: Materializer, ec: ExecutionContext): Route =
    pathPrefix("containers" / Segment) { containerId =>
      path("archive" / Segment) { identifier =>
        entity(as[Multipart.FormData]) { (formData: Multipart.FormData) =>


          val uploader = ArchiveBuilderStore.getOrCreate(containerId, identifier)

          println(s"uploading: $containerId:$identifier")

          // collect all parts of the multipart as it arrives into a map
          val allPartsFuture: Future[Map[String, Any]] = formData.parts.mapAsync[(String, Any)](1) {

            case body: BodyPart if body.name == "file" =>
              println(s"bodyPart: ${body.name}")
              // stream into a file as the chunks of it arrives and return a future
              // file to where it got stored
              val chunkFile = File.createTempFile("upload", "tmp")
              chunkFile.deleteOnExit()
              println(chunkFile.getAbsolutePath)
              body.entity.dataBytes.runWith(FileIO.toFile(chunkFile)).map(_ => body.name -> chunkFile)

            case body: BodyPart =>
              println(s"bodyPart: ${body.additionalDispositionParams}, ${body.dispositionParams}")
              // collect form field values
              body.toStrict(2.seconds).map(strict => body.name -> strict.entity.data.utf8String)

          }.runFold(Map.empty[String, Any])((map, tuple) => map + tuple)

          // when processing have finished create a response for the user
          onSuccess(allPartsFuture) { allParts =>
            println(s"complete: $allParts")

            val allChunkWasUploaded = uploader.append(allParts)

            if (allChunkWasUploaded) {
              ArchiveBuilderStore.remove(containerId, identifier)
            }

            complete {
              "ok!"
            }
          }
        }
      }
    }
}
