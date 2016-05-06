package com.arielnetworks.ragnalog.port.adapter.http.route

import akka.http.scaladsl.server.Directives._
import akka.stream.Materializer
import ch.megard.akka.http.cors.{CorsDirectives, CorsSettings}
import com.arielnetworks.ragnalog.port.adapter.http.{ArchiveRoute, ContainerRoute, LogFileRoute}

import scala.concurrent.ExecutionContext

class RestRoute extends RouteService with CorsDirectives {

  def route(implicit m: Materializer, ec: ExecutionContext) =
    pathPrefix("api") {
      cors(CorsSettings.defaultSettings.copy(allowCredentials = false)) {
        (new ContainerRoute).route ~
          (new LogFileRoute).route ~
          (new ArchiveRoute).route
      }
    }

}
