package com.arielnetworks.ragnalog.application

import java.util.UUID

import com.arielnetworks.ragnalog.domain.model.container.{Container, ContainerId}

import scala.concurrent.Future

sealed trait ContainerServiceError

final case class InvalidId(message: String) extends ContainerServiceError

final case class AlreadyExists(message: String) extends ContainerServiceError

trait IdPatternSpecification {
  def isSatisfied(id: String): Boolean
}

class ContainerService(idSpec: IdPatternSpecification) {

  def createContainer(containerId: Option[String], containerName: Option[String], containerDescription: Option[String])
  : Future[Either[ContainerServiceError, Container]] = {

    val id = containerId match {
      case Some(x) =>
        if (idSpec.isSatisfied(x)) Right(x)
        else Left(InvalidId(s"Invalid ID: $x"))
      case None => containerName match {
        case Some(x) =>
          if (idSpec.isSatisfied(x)) Right(x)
          else Right(UUID.randomUUID().toString.replace("-", ""))
        case _ => Left(InvalidId(s""))
      }
    }

    val container = id match {
      case Right(x) => {
        val name = containerName match {
          case Some(y) => y
          case _ => x
        }
        Right(new Container(ContainerId(x), name, containerDescription))
        //TODO: containerRepository
      }
      case Left(x) => {
        Left(x)
      }
    }

    Future.successful(container)
  }

  def removeContainer(containerId: ContainerId) = ???

  def activeContainers(): Seq[Container] = ???

  def inactiveContainers(): Seq[Container] = ???
}

