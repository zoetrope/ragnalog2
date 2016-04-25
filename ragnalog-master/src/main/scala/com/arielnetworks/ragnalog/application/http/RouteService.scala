package com.arielnetworks.ragnalog.application.http

import akka.http.scaladsl.server.Route

trait RouteService {
  def route: Route
}
