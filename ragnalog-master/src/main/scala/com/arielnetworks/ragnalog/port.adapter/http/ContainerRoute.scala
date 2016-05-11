package com.arielnetworks.ragnalog.port.adapter.http

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.server.Directives._
import akka.stream.Materializer
import com.arielnetworks.ragnalog.application.ServiceRegistry
import com.arielnetworks.ragnalog.application.container.data._
import com.arielnetworks.ragnalog.domain.model.container.ContainerId
import com.arielnetworks.ragnalog.port.adapter.http.route.RouteService
import spray.json._

import scala.concurrent.ExecutionContext
import scala.util.{Failure, Success}


trait ContainerJsonSupport extends DefaultJsonProtocol with SprayJsonSupport {

  implicit val addContainerRequestFormat = jsonFormat3(AddContainerRequest)
  implicit val addContainerResponseFormat = jsonFormat4(ContainerResponse)
  implicit val updateContainerResponseFormat = jsonFormat2(UpdateContainerRequest)
  implicit val changeContainerResponseFormat = jsonFormat1(ChangeContainerStatusRequest)

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
              .map(list => list.map(c => new ContainerResponse(c.id.id, c.name, c.description, c.status.toString).toJson))
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
              entity(as[ChangeContainerStatusRequest]) { req =>
                // activate/deactivate container
                println(s"change container: $containerId")
                req.status.toLowerCase match {
                  case "active" =>
                    onSuccess(containerService.activateContainer(new ContainerId(containerId))) {
                      case container => complete(container.toJson)
                    }
                  case "inactive" =>
                    onSuccess(containerService.deactivateContainer(new ContainerId(containerId))) {
                      case container => complete(container.toJson)
                    }
                  case _ => complete(500 -> "failed to change container status")
                }
              } ~
                entity(as[UpdateContainerRequest]) { req =>
                  // update container
                  println(s"update container: $containerId")
                  onSuccess(containerService.updateContainer(new ContainerId(containerId), req)) {
                    case container => complete(container.toJson)
                  }
                  complete("ok")
                }
            } ~
            delete {
              // delete container
              complete(s"delete $containerId")
            }
        }
    }
}
