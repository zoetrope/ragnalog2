package com.arielnetworks.ragnalog.port.adapter.http

import akka.http.scaladsl.server.Route
import akka.stream.Materializer

import scala.concurrent.ExecutionContext

trait RouteService {
  def route(implicit m: Materializer, ec: ExecutionContext): Route
}
