package com.arielnetworks.ragnalog.port.adapter.persistence.repository

import com.arielnetworks.ragnalog.domain.model.common.{Entity, Identifier, Repository}
import org.elasticsearch.index.query.QueryBuilder
import rx.lang.scala.Observable


class ElasticsearchRepository[ID <: Identifier[String], E <: Entity[ID]] extends Repository[ID, E] {
  override def add(value: E): Observable[ID] = ???

  override def addOnParent(value: E): Observable[ID] = ???

  override def resolveById(id: ID): Observable[E] = ???

  override def search(query: QueryBuilder): Observable[Seq[E]] = ???
}
