package com.arielnetworks.ragnalog.port.adapter.http

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model.Multipart
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.Materializer
import com.arielnetworks.ragnalog.application.ServiceRegistry
import com.arielnetworks.ragnalog.application.archive.data.{ArchiveResponse, GetArchivesResponse}
import com.arielnetworks.ragnalog.domain.model.archive.ArchiveId
import com.arielnetworks.ragnalog.domain.model.container.ContainerId
import com.arielnetworks.ragnalog.port.adapter.http.route.RouteService
import com.arielnetworks.ragnalog.port.adapter.http.uploader.ArchiveUploader
import spray.json.DefaultJsonProtocol
import spray.json._

import scala.concurrent.ExecutionContext
import scala.util.{Failure, Success}

trait ArchiveJsonSupport extends DefaultJsonProtocol with SprayJsonSupport {
  implicit val archiveResponseFormat = jsonFormat8(ArchiveResponse)
  implicit val archiveListResponseFormat = jsonFormat1(GetArchivesResponse)
}

class ArchiveRoute extends RouteService with ArchiveUploader with ArchiveJsonSupport {

  val archiveService = ServiceRegistry.archiveService


  def route(implicit m: Materializer, ec: ExecutionContext): Route =
    pathPrefix("containers" / Segment) { containerId =>
      pathPrefix("archives") {
        pathEndOrSingleSlash {
          get {
            complete {
              archiveService.archives(ContainerId(containerId)).map(_.toJson)
            }
          }
        } ~
          path(Segment) { archiveId =>
            get {
              //TODO: download archive
              complete("not implemented")
            } ~
              post {
                entity(as[Multipart.FormData]) { (formData: Multipart.FormData) =>
                  upload(containerId, archiveId, formData) { info =>
                    archiveService.registerArchive(info)
                      .onComplete{
                        //TODO: error handling
                        case Success(_)=> println("registerArchive ok")
                        case Failure(e) => e.printStackTrace()
                      }

                    //TODO: send ArchiveRegisteredEvent via WebSocket
                  }
                }
              } ~
              delete {
                onSuccess(archiveService.removeArchive(ArchiveId(archiveId, containerId))){
                  case  archive => complete(archive.toJson)
                }
              }
          }
      }
    }
}
