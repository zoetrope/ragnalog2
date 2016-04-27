package com.arielnetworks.ragnalog.port.adapter.http

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.server.Directives._
import com.arielnetworks.ragnalog.application.archive.data.GetContainersResult
import spray.json.DefaultJsonProtocol._
import spray.json._

class ContainerRoute extends RouteService {

  implicit val containersFormat = jsonFormat2(GetContainersResult)

  def route =
    pathPrefix("containers") {
      pathEndOrSingleSlash {
        get {
          // get containers
          complete(new GetContainersResult("test", "hogehoge").toJson)
        } ~
          post {
            // create new container
            complete("ok")
          }
      } ~
        path(Segment) { containerId =>
          get {
            // get container
            complete(s"get $containerId")
          } ~
            put {
              // update container
              complete(s"update $containerId")
            } ~
            delete {
              // delete container
              complete(s"delete $containerId")
            }
        }
    }
}
