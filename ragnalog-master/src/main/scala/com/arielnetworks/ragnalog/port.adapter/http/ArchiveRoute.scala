package com.arielnetworks.ragnalog.port.adapter.http

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model.Multipart
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.Materializer
import com.arielnetworks.ragnalog.application.ServiceRegistry
import com.arielnetworks.ragnalog.application.archive.data.{ArchiveResponse, GetArchivesResponse}
import com.arielnetworks.ragnalog.port.adapter.http.route.RouteService
import com.arielnetworks.ragnalog.port.adapter.http.uploader.ArchiveUploader
import spray.json.DefaultJsonProtocol
import spray.json._

import scala.concurrent.ExecutionContext

trait ArchiveJsonSupport extends DefaultJsonProtocol with SprayJsonSupport {
  implicit val archiveResponseFormat = jsonFormat7(ArchiveResponse)
  implicit val archiveListResponseFormat = jsonFormat1(GetArchivesResponse)
}

class ArchiveRoute extends RouteService with ArchiveUploader with ArchiveJsonSupport {

  val userService = ServiceRegistry.userService


  def route(implicit m: Materializer, ec: ExecutionContext): Route =
    pathPrefix("containers" / Segment) { containerId =>
      pathPrefix("archives") {
        pathEndOrSingleSlash {
          get {
            complete {
              userService.archives(containerId).map(r => r.toJson)
            }
          }
        } ~
          path(Segment) { identifier =>
            post {
              entity(as[Multipart.FormData]) { (formData: Multipart.FormData) =>
                upload(containerId, identifier, formData) { info =>
                  userService.uploadArchiveFile(info)

                  //TODO: send ArchiveRegisteredEvent via WebSocket
                }
              }
            }
          }
      }
    }
}
