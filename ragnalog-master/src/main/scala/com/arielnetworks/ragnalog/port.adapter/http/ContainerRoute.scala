package com.arielnetworks.ragnalog.port.adapter.http

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.server.Directives._
import akka.stream.Materializer
import com.arielnetworks.ragnalog.application.archive.data.GetContainersResult
import com.arielnetworks.ragnalog.port.adapter.http.route.RouteService
import spray.json.DefaultJsonProtocol._
import spray.json._

import scala.concurrent.ExecutionContext

class ContainerRoute extends RouteService {

  implicit val containersFormat = jsonFormat2(GetContainersResult)

  def route(implicit m: Materializer, ec: ExecutionContext) =
    pathPrefix("containers") {
      pathEndOrSingleSlash {
        get {
          // get containers
          complete(new GetContainersResult("test", "hogehoge").toJson)
        } ~
          post {
            println("add container")
            // create new container
            complete(new GetContainersResult("test", "hogehoge").toJson)
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
