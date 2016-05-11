package com.arielnetworks.ragnalog.application.container

import com.arielnetworks.ragnalog.application.archive.ArchiveService
import com.arielnetworks.ragnalog.application.container.data.{AddContainerRequest, ContainerResponse, UpdateContainerRequest}
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
      .map(_ => new ContainerResponse(container.id.id, container.name, container.description, container.status.toString))
  }

  def removeContainer(containerId: ContainerId): Future[Unit] = {
    for {
      container <- containerRepository.resolveById(containerId)
      _ <- archiveService.removeAll(containerId)
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

  def updateContainer(containerId: ContainerId, req: UpdateContainerRequest): Future[ContainerResponse] = {
    //TODO: if name is null or empty, then failed to update
    for {
      container <- containerRepository.resolveById(containerId)
      updatedContainer = container.change(req.name, req.description)
      _ <- containerRepository.save(updatedContainer)
    } yield new ContainerResponse(updatedContainer.id.id, updatedContainer.name, updatedContainer.description, updatedContainer.status.toString)
  }

  def activateContainer(containerId: ContainerId): Future[ContainerResponse] = {
    for {
      container <- containerRepository.resolveById(containerId)
      updatedContainer = container.activate()
      //      _ <- archiveService.activateAll(containerId)
      _ <- containerRepository.save(updatedContainer)
    } yield new ContainerResponse(updatedContainer.id.id, updatedContainer.name, updatedContainer.description, updatedContainer.status.toString)
  }

  def deactivateContainer(containerId: ContainerId): Future[ContainerResponse] = {
    for {
      container <- containerRepository.resolveById(containerId)
      updatedContainer = container.deactivate()
      //      _ <- archiveService.deactivateAll(containerId)
      _ <- containerRepository.save(updatedContainer)
    } yield new ContainerResponse(updatedContainer.id.id, updatedContainer.name, updatedContainer.description, updatedContainer.status.toString)
  }


}

