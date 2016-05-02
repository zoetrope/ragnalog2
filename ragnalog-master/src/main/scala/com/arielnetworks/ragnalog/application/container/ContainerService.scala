package com.arielnetworks.ragnalog.application.container

import java.util.UUID

import com.arielnetworks.ragnalog.domain.model.container.{Container, ContainerId, ContainerRepository, ContainerStatus}
import com.arielnetworks.ragnalog.domain.model.rawfile.RawFileService
import com.arielnetworks.ragnalog.domain.model.registration.RegistrationAdapter
import com.arielnetworks.ragnalog.domain.model.visualization.VisualizationAdapter

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

trait IdPatternSpecification {
  def isSatisfied(id: String): Boolean
}

class ContainerService
(
  containerRepository: ContainerRepository,
  visualizationAdapter: VisualizationAdapter,
  registrationAdapter: RegistrationAdapter,
  logFileService: RawFileService,
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
        val container = new Container(ContainerId(r), name, containerDescription, ContainerStatus.Active)
        containerRepository.add(container).map(_ => container)
      case Left(x) => Future.failed(x)
    }
  }

  def removeContainer(containerId: ContainerId): Future[Unit] = {
    for {
      container <- containerRepository.resolveById(containerId)
      _ <- visualizationAdapter.removeContainer(container)
      _ <- registrationAdapter.remove(container)
      _ <- logFileService.removeAll(container)
      _ <- containerRepository.deleteById(containerId)
    } yield Unit
  }

  def activeContainers(): Future[Seq[Container]] = {
    for {
      count <- containerRepository.countByStatus(ContainerStatus.Active)
      containers <- containerRepository.searchByStatus(0, count.asInstanceOf[Int], ContainerStatus.Active)
    } yield containers
  }

  def inactiveContainers(): Future[Seq[Container]] = {
    for {
      count <- containerRepository.countByStatus(ContainerStatus.Inactive)
      containers <- containerRepository.searchByStatus(0, count.asInstanceOf[Int], ContainerStatus.Inactive)
    } yield containers
  }

  def updateContainer(containerId: ContainerId, name: Option[String], description: Option[String]): Future[Unit] = {
    for {
      container <- containerRepository.resolveById(containerId)
      _ <- containerRepository.save(new Container(containerId, name.getOrElse(container.name), description, container.status))
    } yield Unit
  }

  def activateContainer(containerId: ContainerId): Future[Unit] = {
    for {
      container <- containerRepository.resolveById(containerId)
      _ <- container.activate()
      _ <- visualizationAdapter.addContainer(container)
      _ <- registrationAdapter.open(container)
      _ <- container.save()
    } yield Unit
  }

  def deactivateContainer(containerId: ContainerId): Future[Unit] = {
    for {
      container <- containerRepository.resolveById(containerId)
      _ <- container.deactivate()
      _ <- visualizationAdapter.removeContainer(container)
      _ <- registrationAdapter.close(container)
      _ <- container.save()
    } yield Unit
  }


}

