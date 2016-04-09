package com.arielnetworks.ragnalog.domain.model.common

import scala.concurrent.Future

trait Repository[ID <: Identifier[_], E <: Entity[ID]] {

  def add(value: E): Future[ID]

  def resolveById(id: ID): Future[E]

  def update(value: E): Future[ID]

  def deleteById(id: ID): Future[E]

}
