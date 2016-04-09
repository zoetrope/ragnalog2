package com.arielnetworks.ragnalog.port.adapter.persistence.repository

import com.arielnetworks.ragnalog.domain.model.common.{Entity, Identifier, Repository}

import scala.concurrent.Future

abstract class RepositoryOnElasticsearch[ID <: Identifier[String], E <: Entity[ID]] extends Repository[ID, E] {
  override def add(value: E): Future[ID] = ???

  override def update(value: E): Future[ID] = ???

  override def deleteById(id: ID): Future[E] = ???

  override def resolveById(id: ID): Future[E] = ???

//  protected def convert(source:)
}
