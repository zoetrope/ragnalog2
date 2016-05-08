package com.arielnetworks.ragnalog.port.adapter.http.route

import akka.http.scaladsl.server.Directives._
import akka.stream.Materializer
import ch.megard.akka.http.cors.{CorsDirectives, CorsSettings}
import com.arielnetworks.ragnalog.port.adapter.http.{AppRoute, ArchiveRoute, ContainerRoute, LogFileRoute}

import scala.concurrent.ExecutionContext
import akka.http.scaladsl.model.HttpMethods._
import scala.collection.immutable.Seq

class RestRoute extends RouteService with CorsDirectives {

  def route(implicit m: Materializer, ec: ExecutionContext) =
    pathPrefix("api") {
      cors(CorsSettings.defaultSettings.copy(allowedMethods = Seq(GET, POST, PUT, DELETE, HEAD, OPTIONS), allowCredentials = false)) {
        (new ContainerRoute).route ~
          (new LogFileRoute).route ~
          (new ArchiveRoute).route ~
          (new AppRoute).route
      }
    }

}
