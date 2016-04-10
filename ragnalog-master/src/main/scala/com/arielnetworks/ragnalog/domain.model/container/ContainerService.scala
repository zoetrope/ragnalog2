package com.arielnetworks.ragnalog.domain.model.container

import scala.concurrent.{ExecutionContext, Future}

class ContainerService
(
  containerRepository: ContainerRepository
) {

  import ExecutionContext.Implicits.global

  def createContainer(containerId: ContainerId, containerName: String, containerDescription: Option[String])
  : Future[Container] = {
    val container = new Container(containerId, containerName, containerDescription, ContainerStatus.Active)
    containerRepository.add(container).map(_ => container)
  }

  def removeContainer(containerId: ContainerId) = ???

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
}

