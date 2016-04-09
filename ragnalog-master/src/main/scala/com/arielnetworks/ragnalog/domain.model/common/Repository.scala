package com.arielnetworks.ragnalog.domain.model.common

import scala.concurrent.Future

trait Repository[ID <: Identifier[_], E <: Entity[ID]] {

  def add(entity: E): Future[Unit]

  def resolveById(id: ID): Future[E]

  def update(value: E): Future[Unit]

  def deleteById(id: ID): Future[Unit]

}
