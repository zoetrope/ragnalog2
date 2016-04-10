package com.arielnetworks.ragnalog.application

import java.util.UUID

import com.arielnetworks.ragnalog.domain.model.container.{Container, ContainerId, ContainerRepository}

import scala.concurrent.{ExecutionContext, Future, Promise}
import scala.util.Failure

trait IdPatternSpecification {
  def isSatisfied(id: String): Boolean
}

class ContainerService
(
  idSpec: IdPatternSpecification,
  containerRepository: ContainerRepository
) {

  import ExecutionContext.Implicits.global

  def createContainer(containerId: Option[String], containerName: Option[String], containerDescription: Option[String])
  : Future[Container] = {

    val id = containerId match {
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

    id match {
      case Right(r) =>
        val name = containerName match {
          case Some(x) => x
          case _ => r
        }
        val container = new Container(ContainerId(r), name, containerDescription, true)
        containerRepository.add(container).map(_ => container)
      case Left(x) => Future.failed(x)
    }
  }

  def removeContainer(containerId: ContainerId) = ???

  def activeContainers(): Seq[Container] = ???

  def inactiveContainers(): Seq[Container] = ???
}

