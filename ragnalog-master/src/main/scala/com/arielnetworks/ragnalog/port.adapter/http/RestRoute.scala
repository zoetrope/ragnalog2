package com.arielnetworks.ragnalog.port.adapter.http

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.Materializer

import scala.concurrent.ExecutionContext

class RestRoute extends RouteService with CorsSupport {

  def route(implicit m: Materializer, ec: ExecutionContext) =
    pathPrefix("api") {
      corsHandler {
        (new ContainerRoute).route ~
          (new FileRoute).route ~
          (new ArchiveUploaderRoute).route
      }
    }

}
