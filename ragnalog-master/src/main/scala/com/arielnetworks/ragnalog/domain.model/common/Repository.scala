package com.arielnetworks.ragnalog.domain.model.common

import rx.lang.scala.Observable
import org.elasticsearch.index.query.QueryBuilder

trait Repository[ID <: Identifier[_], E <: Entity[ID]] {

  def add(value: E): Observable[ID]

  def addOnParent(value: E): Observable[ID]

  def resolveById(id: ID): Observable[E]

  //TODO: QueryBuilderへの依存をなくす
  def search(query: QueryBuilder): Observable[Seq[E]]
}
