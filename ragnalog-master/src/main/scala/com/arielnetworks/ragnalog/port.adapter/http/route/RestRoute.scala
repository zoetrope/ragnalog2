package com.arielnetworks.ragnalog.port.adapter.http.route

import akka.http.scaladsl.server.Directives._
import akka.stream.Materializer
import com.arielnetworks.ragnalog.port.adapter.http.uploader.ArchiveUploaderRoute
import com.arielnetworks.ragnalog.port.adapter.http.{ContainerRoute, CorsSupport, FileRoute}

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
