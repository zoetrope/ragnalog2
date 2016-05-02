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


trait AddContainerSupport extends DefaultJsonProtocol with SprayJsonSupport {

  implicit val addContainerRequestFormat = jsonFormat3(AddContainerRequest)
  implicit val addContainerResponseFormat = jsonFormat4(ContainerResponse)

  implicit val containersFormat = jsonFormat1(GetContainersResult)
}

class ContainerRoute extends RouteService with AddContainerSupport {

  val administrationService = ServiceRegistry.administrationService

  def route(implicit m: Materializer, ec: ExecutionContext) =
    pathPrefix("containers") {
      pathEndOrSingleSlash {
        get {
          // get containers
          complete {
            administrationService.activeContainers()
              .map(list => list.map(c => new ContainerResponse(c.id.value, c.name, c.description, c.status.toString).toJson))
          }
        } ~
          (post & entity(as[AddContainerRequest])) { req =>
            println(s"add container: $req")
            val container = administrationService.createContainer(Some(req.id), Some(req.name), req.description)

            // create new container
            onComplete(container) {
              case Success(c) => complete(new ContainerResponse(c.id.value, c.name, c.description, c.status.toString).toJson)
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
