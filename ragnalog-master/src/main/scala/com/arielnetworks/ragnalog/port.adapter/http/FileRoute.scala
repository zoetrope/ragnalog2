package com.arielnetworks.ragnalog.port.adapter.http

import akka.http.scaladsl.server.Directives._
import akka.stream.Materializer
import com.arielnetworks.ragnalog.port.adapter.http.route.RouteService

import scala.concurrent.ExecutionContext

class FileRoute extends RouteService {

  def route(implicit m: Materializer, ec: ExecutionContext) =
    pathPrefix("file") {
      get {
        complete("")
      }
    }
}
