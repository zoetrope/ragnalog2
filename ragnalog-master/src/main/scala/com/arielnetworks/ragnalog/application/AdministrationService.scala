package com.arielnetworks.ragnalog.application

import java.util.UUID

import com.arielnetworks.ragnalog.domain.model.container.{Container, ContainerId, ContainerService, ContainerStatus}

import scala.concurrent.Future

trait IdPatternSpecification {
  def isSatisfied(id: String): Boolean
}

class AdministrationService
(
  containerService: ContainerService,
  idSpec: IdPatternSpecification
) {

  def createContainer(containerId: Option[String], containerName: Option[String], containerDescription: Option[String])
  : Future[Container] = {

    val idOrError = containerId match {
      case Some(x) =>
        if (idSpec.isSatisfied(x)) Right(x)
        else Left(new IllegalArgumentException(s"Invalid ID: $x"))
      case None => containerName match {
        case Some(x) =>
          if (idSpec.isSatisfied(x)) Right(x)
          else Right(UUID.randomUUID().toString.replace("-", ""))
        case _ => Left(new IllegalArgumentException("either id or name is required"))
      }
    }

    idOrError match {
      case Right(r) =>
        val name = containerName match {
          case Some(x) => x
          case _ => r
        }
        containerService.createContainer(ContainerId(r), name, containerDescription)
      case Left(x) => Future.failed(x)
    }
  }

  def removeContainer(containerId: ContainerId) = {
    containerService.removeContainer(containerId)
  }

  def activeContainers(): Future[Seq[Container]] = {
    containerService.activeContainers()
  }

  def inactiveContainers(): Future[Seq[Container]] = {
    containerService.inactiveContainers()
  }
}
