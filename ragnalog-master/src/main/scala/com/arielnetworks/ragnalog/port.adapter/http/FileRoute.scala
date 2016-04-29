package com.arielnetworks.ragnalog.port.adapter.http

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.Materializer
import com.arielnetworks.ragnalog.application.archive.data.GetContainersResult
import com.arielnetworks.ragnalog.port.adapter.http.route.RouteService
import spray.json.DefaultJsonProtocol._
import spray.json._

import scala.concurrent.ExecutionContext

class FileRoute extends RouteService {

  implicit val containersFormat = jsonFormat2(GetContainersResult)

  def route(implicit m: Materializer, ec: ExecutionContext) =
    pathPrefix("file") {
      get {
        complete(new GetContainersResult("test", "hogehoge").toJson)
      }
    }
}
