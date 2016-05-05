package com.arielnetworks.ragnalog.port.adapter.http

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.server.Directives._
import akka.stream.Materializer
import com.arielnetworks.ragnalog.application.ServiceRegistry
import com.arielnetworks.ragnalog.application.container.data.{AddContainerRequest, ContainerResponse, GetContainersResult}
import com.arielnetworks.ragnalog.port.adapter.http.route.RouteService
import spray.json._

import scala.concurrent.ExecutionContext
import scala.util.{Failure, Success}


trait ContainerJsonSupport extends DefaultJsonProtocol with SprayJsonSupport {

  implicit val addContainerRequestFormat = jsonFormat3(AddContainerRequest)
  implicit val addContainerResponseFormat = jsonFormat4(ContainerResponse)

  implicit val containersFormat = jsonFormat1(GetContainersResult)
}

class ContainerRoute extends RouteService with ContainerJsonSupport {

  val containerService = ServiceRegistry.containerService

  def route(implicit m: Materializer, ec: ExecutionContext) =
    pathPrefix("containers") {
      pathEndOrSingleSlash {
        get {
          complete {
            //TODO: add converter (and move to application layer)
            containerService.activeContainers()
              .map(list => list.map(c => new ContainerResponse(c.id.value, c.name, c.description, c.status.toString).toJson))
          }
        } ~
          (post & entity(as[AddContainerRequest])) { req =>
            println(s"add container: $req")
            val container = containerService.createContainer(req)

            onComplete(container) {
              case Success(c) => complete(c.toJson)
              case Failure(e) =>
                println("failed to add container")
                complete(500 -> "failed to add container")
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
