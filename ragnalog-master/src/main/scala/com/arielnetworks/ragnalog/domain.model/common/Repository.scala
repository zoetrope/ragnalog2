package com.arielnetworks.ragnalog.domain.model.common

import scala.concurrent.Future

trait Repository[ID <: Identifier[_], E <: Entity[ID], PARENT <: Identifier[_]] {

  def add(entity: E, parentId: Option[PARENT] = None): Future[Unit]

  def exists(id: ID): Future[Boolean]

  def save(entity: E): Future[Unit]

  def resolveById(id: ID): Future[E]

  def deleteById(id: ID): Future[Unit]

}
