package com.arielnetworks.ragnalog.port.adapter.http

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.Materializer
import com.arielnetworks.ragnalog.application.ServiceRegistry
import com.arielnetworks.ragnalog.application.logfile.data._
import com.arielnetworks.ragnalog.port.adapter.http.route.RouteService
import com.arielnetworks.ragnalog.port.adapter.http.uploader.ArchiveUploader
import spray.json.{DefaultJsonProtocol, _}

import scala.concurrent.ExecutionContext
import scala.util.{Failure, Success}

trait LogFileJsonSupport extends DefaultJsonProtocol with SprayJsonSupport {
  implicit val logFileResponseFormat = jsonFormat11(LogFileResponse)
  implicit val logFileListResponseFormat = jsonFormat3(GetLogFilesResponse)

  implicit val registerLogFileRequestFormat = jsonFormat4(RegisterLogFileRequest)
  implicit val registerLogFileResponseFormat = jsonFormat1(RegisterLogFileResponse)

  implicit val previewResponseFormat = jsonFormat3(PreviewResponse)
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
          } ~
            (put & entity(as[Seq[RegisterLogFileRequest]])) { req =>
              println(req)
              onSuccess(logFileService.registerLogFile(req)) {
                case res => complete(res.toJson)
              }
            }
        } ~
          path(Segment) { logFileId =>
            parameters('archiveId) { (archiveId) =>
              get {
                complete(
                  logFileService.preview(containerId, archiveId, logFileId)
                )
              } ~
                delete {
                  //TODO: delete logFile
                  complete("not implemented")
                }
            }
          }
      }
    }
}
