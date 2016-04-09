package com.arielnetworks.ragnalog.domain.model.common

import scala.concurrent.Future

sealed trait RepositoryError
sealed trait AdditionError

final case class InfrastructureError(ex: Throwable) extends RepositoryError with AdditionError

final case class AlreadyExists() extends AdditionError

trait Repository[ID <: Identifier[_], E <: Entity[ID]] {

  def add(entity: E): Future[Either[AdditionError, Unit]]

  def resolveById(id: ID): Future[E]

  def update(value: E): Future[ID]

  def deleteById(id: ID): Future[E]

}
