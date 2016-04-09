package com.arielnetworks.ragnalog.domain.model.common

import scala.concurrent.Future

trait Repository[ID <: Identifier[_], E <: Entity[ID], CTX <: EntityIOContext] {

  def add(value: E)(implicit context: CTX): Future[ID]

  def resolveById(id: ID)(implicit context: CTX): Future[E]

  def update(value: E)(implicit context: CTX): Future[ID]

  def deleteById(id: ID)(implicit context: CTX): Future[E]

}
