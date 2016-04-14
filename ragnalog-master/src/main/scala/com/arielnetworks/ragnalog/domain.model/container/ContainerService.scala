package com.arielnetworks.ragnalog.domain.model.container

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class ContainerService
(
  containerRepository: ContainerRepository
) {

  def createContainer(containerId: ContainerId, containerName: String, containerDescription: Option[String])
  : Future[Container] = {
    val container = new Container(containerId, containerName, containerDescription, ContainerStatus.Active)
    containerRepository.add(container).map(_ => container)
  }

  def removeContainer(containerId: ContainerId): Future[Unit] = {
    containerRepository.deleteById(containerId)
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

  def resolvedById(containerId: ContainerId): Future[Container] = {
    containerRepository.resolveById(containerId)
  }

  def updateContainer(containerId: ContainerId, name: Option[String], description: Option[String]): Future[Unit] = {
    for {
      container <- resolvedById(containerId)
      _ <- containerRepository.save(new Container(containerId, name.getOrElse(container.name), description, container.status))
    } yield Unit
  }
}

