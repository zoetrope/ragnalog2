package com.arielnetworks.ragnalog.application

import java.util.UUID

import com.arielnetworks.ragnalog.domain.model.archive.LogFileService
import com.arielnetworks.ragnalog.domain.model.container.{Container, ContainerId, ContainerService, ContainerStatus}
import com.arielnetworks.ragnalog.domain.model.registration.RegistrationService
import com.arielnetworks.ragnalog.domain.model.visualization.VisualizationService

import scala.concurrent.Future

trait IdPatternSpecification {
  def isSatisfied(id: String): Boolean
}

class AdministrationService
(
  containerService: ContainerService,
  visualizationService: VisualizationService,
  registrationService: RegistrationService,
  logFileService: LogFileService,
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

  def removeContainer(containerId: ContainerId): Future[Unit] = {
    for {
      container <- containerService.resolvedById(containerId)
      visualizationService.removeContainer(container)
      registrationService.remove(container)
      logFileService.removeAll(container)
      containerService.removeContainer(containerId)
    } yield new Unit
  }

  def activeContainers(): Future[Seq[Container]] = {
    containerService.activeContainers()
  }

  def inactiveContainers(): Future[Seq[Container]] = {
    containerService.inactiveContainers()
  }

  def updateContainer(containerId: ContainerId, name: String, description: String): Future[Unit] = {
    for {
      container <- containerService.resolvedById(containerId)
      container.name = name
      container.description = description
      container.save()
    } yield new Unit
  }

  def activateContainer(containerId: ContainerId): Future[Unit] = {
    for {
      container <- containerService.resolvedById(containerId)
      container.activate()
      visualizationService.addContainer(container)
      registrationService.open(container)
      container.save()
    } yield new Unit
  }

  def deactivateContainer(containerId: ContainerId): Future[Unit] = {
    for {
      container <- containerService.resolvedById(containerId)
      container.deactivate()
      visualizationService.removeContainer(container)
      registrationService.close(container)
      container.save()
    } yield new Unit
  }
}
