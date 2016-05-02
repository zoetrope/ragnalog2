package com.arielnetworks.ragnalog.port.adapter.persistence.repository

import com.arielnetworks.ragnalog.domain.model.common._
import com.arielnetworks.ragnalog.port.adapter.persistence.translator.Translator
import com.sksamuel.elastic4s.ElasticClient
import com.sksamuel.elastic4s.ElasticDsl._
import org.elasticsearch.action.index.IndexRequest.OpType

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Future, Promise}
import scala.util.{Failure, Success}

abstract class RepositoryOnElasticsearch[ID <: Identifier[String], E <: Entity[ID], PARENT <: Identifier[String]]
(
  protected val elasticClient: ElasticClient,
  protected val indexName: String,
  protected val typeName: String
)
  extends Repository[ID, E, PARENT]
    with Translator[ID, E] {

  override def add(entity: E, parentId: Option[PARENT]): Future[Unit] = {
    val p = Promise[Unit]()
    try {
      val ret = parentId match {
        case Some(p) =>
          elasticClient.execute(
            index into indexName / typeName id entity.id.value parent p.value opType OpType.CREATE fields toFieldsFromEntity(entity)
          )
        case None =>
          elasticClient.execute(
            index into indexName / typeName id entity.id.value opType OpType.CREATE fields toFieldsFromEntity(entity)
          )
      }

      ret onComplete {
        case Success(r) =>
          if (r.created) p.success(Unit)
          else p.failure(new RepositoryIOException("could not create entity."))
        case Failure(e) => p.failure(e)
      }
    } catch {
      case e: Throwable => p.failure(e)
    }
    p.future
  }

  override def save(entity: E): Future[Unit] = {
    val p = Promise[Unit]()
    try {
      elasticClient.execute(
        update(entity.id.value) in indexName / typeName doc toFieldsFromEntity(entity)
      ) onComplete {
        case Success(r) => p.success(Unit)
        case Failure(e) => p.failure(e)
      }
    } catch {
      case e: Throwable => p.failure(e)
    }
    p.future
  }

  override def deleteById(id: ID): Future[Unit] = {
    val p = Promise[Unit]()
    try {
      elasticClient.execute(
        delete(id.value) from indexName / typeName
      ) onComplete {
        case Success(r) =>
          if (r.isFound) p.success(Unit)
          else p.failure(new RepositoryIOException(s"could not delete entity(id:${id.value})."))
        case Failure(e) => p.failure(e)
      }
    } catch {
      case e: Throwable => p.failure(e)
    }
    p.future
  }

  override def resolveById(id: ID): Future[E] = {
    val p = Promise[E]()
    try {
      elasticClient.execute(
        get id id.value from indexName / typeName
      ) onComplete {
        case Success(r) =>
          if (r.isExists) p.success(toEntityFromFields(r.getId, r.source))
          else p.failure(new RepositoryIOException(s"could not resolve entity(id:${id.value})."))
        case Failure(e) => p.failure(e)
      }
    } catch {
      case e: Throwable => p.failure(e)
    }
    p.future
  }
}
