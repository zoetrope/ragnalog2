package com.arielnetworks.ragnalog.application.http

import akka.http.scaladsl.server.Directives._

class RestRoute extends RouteService {

  def route = {
    pathPrefix("api") {
      (new ContainerRoute).route ~
        (new FileRoute).route
    }
  }
}
