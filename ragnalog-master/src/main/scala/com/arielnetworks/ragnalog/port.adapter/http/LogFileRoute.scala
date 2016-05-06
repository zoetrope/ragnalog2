package com.arielnetworks.ragnalog.port.adapter.http

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model.Multipart
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.Materializer
import com.arielnetworks.ragnalog.application.ServiceRegistry
import com.arielnetworks.ragnalog.application.archive.data.{ArchiveResponse, GetArchivesResponse}
import com.arielnetworks.ragnalog.application.logfile.data.{GetLogFilesResponse, LogFileResponse}
import com.arielnetworks.ragnalog.port.adapter.http.route.RouteService
import com.arielnetworks.ragnalog.port.adapter.http.uploader.ArchiveUploader
import spray.json.DefaultJsonProtocol
import spray.json._

import scala.concurrent.ExecutionContext
import scala.util.{Failure, Success}

trait LogFileJsonSupport extends DefaultJsonProtocol with SprayJsonSupport {
  implicit val logFileResponseFormat = jsonFormat10(LogFileResponse)
  implicit val logFileListResponseFormat = jsonFormat3(GetLogFilesResponse)
}

class LogFileRoute extends RouteService with ArchiveUploader with LogFileJsonSupport {

  val logFileService = ServiceRegistry.logFileService


  def route(implicit m: Materializer, ec: ExecutionContext): Route =
    pathPrefix("containers" / Segment) { containerId =>
      pathPrefix("logfiles") {
        pathEndOrSingleSlash {
          get {
            parameters('archiveId.?, 'status.?, 'name.?, 'page.as[Int] ? 0) { (archiveId, status, name, page) =>
              complete {
                logFileService.search(containerId, archiveId, status, name, page).map(_.toJson)
              }
            }
          }
        } ~
          path(Segment) { identifier =>
            get {
              //TODO: download archive
              complete("not implemented")
            } ~
              post {
                complete("not implemented")
              } ~
              delete {
                //TODO: delete archive
                complete("not implemented")
              }
          }
      }
    }
}
