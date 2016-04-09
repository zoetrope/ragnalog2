package com.arielnetworks.ragnalog.port.adapter.persistence.repository

import com.arielnetworks.ragnalog.domain.model.common._
import com.sksamuel.elastic4s.ElasticClient
import com.sksamuel.elastic4s.ElasticDsl._
import org.elasticsearch.action.index.IndexRequest.OpType

import scala.concurrent.{ExecutionContext, Future, Promise}
import scala.util.{Failure, Success}


abstract class RepositoryOnElasticsearch[ID <: Identifier[String], E <: Entity[ID]]
(
  elasticClient: ElasticClient,
  indexName: String,
  typeName: String
)
  extends Repository[ID, E]
    with Translator[ID, E] {

  import ExecutionContext.Implicits.global

  override def add(entity: E): Future[Unit] = {
    val p = Promise[Unit]()
    try {
      val f = elasticClient.execute(
        index into indexName / typeName id entity.id.value opType OpType.CREATE fields toFieldsFromEntity(entity)
      )
      f.onComplete {
        case Success(r) => if (r.created) p.success() else p.failure(new RepositoryIOException("document was not created"))
        case Failure(e) => p.failure(e)
      }
    } catch {
      case e: Throwable => p.failure(e)
    }
    p.future
  }

  override def update(value: E): Future[Unit] = ???

  override def deleteById(id: ID): Future[Unit] = ???

  override def resolveById(id: ID): Future[E] = ???
}
