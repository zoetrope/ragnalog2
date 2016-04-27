package com.arielnetworks.ragnalog.port.adapter.http

import akka.http.scaladsl.server.Directives._

class RestRoute extends RouteService with CorsSupport{

  def route = {
    pathPrefix("api") {
      corsHandler {
        (new ContainerRoute).route ~
          (new FileRoute).route
      }
    }
  }
}
