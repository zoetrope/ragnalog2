package com.arielnetworks.ragnalog.port.adapter.persistence.repository

import com.arielnetworks.ragnalog.domain.model.common.{Entity, EntityIOContext, Identifier, Repository}

import scala.concurrent.Future

case class EntityIOContextOnElasticsearch() extends EntityIOContext

abstract class RepositoryOnElasticsearch[ID <: Identifier[String], E <: Entity[ID], CTX <: EntityIOContextOnElasticsearch] extends Repository[ID, E, CTX] {
  override def add(value: E)(implicit context: CTX): Future[ID] = ???

  override def update(value: E)(implicit context: CTX): Future[ID] = ???

  override def deleteById(id: ID)(implicit context: CTX): Future[E] = ???

  override def resolveById(id: ID)(implicit context: CTX): Future[E] = ???

//  protected def convert(source:)
}
