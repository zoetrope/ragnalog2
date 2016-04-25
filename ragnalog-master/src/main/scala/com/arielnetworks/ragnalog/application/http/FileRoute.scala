package com.arielnetworks.ragnalog.application.http

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.server.Directives._
import com.arielnetworks.ragnalog.application.archive.data.GetContainersResult
import spray.json.DefaultJsonProtocol._
import spray.json._

class FileRoute extends RouteService {

  implicit val containersFormat = jsonFormat2(GetContainersResult)

  def route =
    pathPrefix("file") {
      get {
        complete(new GetContainersResult("test", "hogehoge").toJson)
      }
    }
}
