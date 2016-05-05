package com.arielnetworks.ragnalog.application.container

import com.arielnetworks.ragnalog.application.archive.ArchiveService
import com.arielnetworks.ragnalog.application.container.data.{AddContainerRequest, ContainerResponse}
import com.arielnetworks.ragnalog.domain.model.container.{Container, ContainerId, ContainerRepository, ContainerStatus}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

trait IdPatternSpecification {
  def isSatisfied(id: String): Boolean
}

class ContainerService
(
  containerRepository: ContainerRepository,
  archiveService: ArchiveService,
  idSpec: IdPatternSpecification
) {

  def createContainer(req: AddContainerRequest): Future[ContainerResponse] = {

    if (!idSpec.isSatisfied(req.id)) {
      println("is not satisfied")
      return Future.failed(new IllegalArgumentException(s"Invalid ID: ${req.id}"))
    }
    val name = req.name.getOrElse(req.id)

    val container = new Container(ContainerId(req.id), name, req.description, ContainerStatus.Active)
    containerRepository
      .add(container)
      .map(_ => new ContainerResponse(container.id.value, container.name, container.description, container.status.toString))
  }

  def removeContainer(id: String): Future[Unit] = {
    val containerId = ContainerId(id)
    for {
      container <- containerRepository.resolveById(containerId)
      _ <- archiveService.removeAll(id)
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
      _ <- archiveService.activateAll(containerId)
      _ <- containerRepository.save(container)
    } yield Unit
  }

  def deactivateContainer(containerId: ContainerId): Future[Unit] = {
    for {
      container <- containerRepository.resolveById(containerId)
      _ <- container.deactivate()
      _ <- archiveService.deactivateAll(containerId)
      _ <- containerRepository.save(container)
    } yield Unit
  }


}

