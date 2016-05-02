package com.arielnetworks.ragnalog.port.adapter.http

import akka.http.scaladsl.model.Multipart
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.Materializer
import com.arielnetworks.ragnalog.application.ServiceRegistry
import com.arielnetworks.ragnalog.port.adapter.http.route.RouteService
import com.arielnetworks.ragnalog.port.adapter.http.uploader.ArchiveUploader

import scala.concurrent.ExecutionContext

class ArchiveRoute extends RouteService with ArchiveUploader {

  val userService = ServiceRegistry.userService

  def route(implicit m: Materializer, ec: ExecutionContext): Route =
    pathPrefix("containers" / Segment) { containerId =>
      pathPrefix("archives") {
        pathEndOrSingleSlash {
          get {
            complete("ok")
          }
        } ~
          path(Segment) { identifier =>
            post {
              entity(as[Multipart.FormData]) { (formData: Multipart.FormData) =>
                upload(containerId, identifier, formData)
              }
            }
          }
      }
    }
}
