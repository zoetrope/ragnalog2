package com.arielnetworks.ragnalog.port.adapter.http

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.model.HttpEntity
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.unmarshalling.Unmarshaller
import akka.stream.Materializer
import com.arielnetworks.ragnalog.application.archive.data.GetContainersResult
import com.arielnetworks.ragnalog.application.container.data.{AddContainerRequest, AddContainerResponse}
import com.arielnetworks.ragnalog.port.adapter.http.route.RouteService
import spray.json.DefaultJsonProtocol._
import spray.json._

import scala.concurrent.ExecutionContext


trait AddContainerSupport extends DefaultJsonProtocol with SprayJsonSupport {
  implicit val containersFormat = jsonFormat2(GetContainersResult)

  implicit val addContainerRequestFormat = jsonFormat3(AddContainerRequest)
  implicit val addContainerResponseFormat = jsonFormat4(AddContainerResponse)
}

class ContainerRoute extends RouteService with AddContainerSupport {


  def route(implicit m: Materializer, ec: ExecutionContext) =
    pathPrefix("containers") {
      pathEndOrSingleSlash {
        get {
          // get containers
          complete(new GetContainersResult("test", "hogehoge").toJson)
        } ~
          post {
            entity(as[AddContainerRequest]) { req =>
              println(s"add container: $req")
              // create new container
              complete(new AddContainerResponse("test", "hogehoge", None, "active").toJson)
            }
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
