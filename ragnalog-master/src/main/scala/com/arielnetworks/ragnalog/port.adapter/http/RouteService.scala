package com.arielnetworks.ragnalog.port.adapter.http

import akka.http.scaladsl.server.Route

trait RouteService {
  def route: Route
}
