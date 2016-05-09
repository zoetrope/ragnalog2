package com.arielnetworks.ragnalog.port.adapter.persistence.repository

import com.arielnetworks.ragnalog.domain.model.common._
import com.arielnetworks.ragnalog.port.adapter.persistence.translator.Translator
import com.sksamuel.elastic4s.ElasticClient
import com.sksamuel.elastic4s.ElasticDsl._
import org.elasticsearch.action.index.IndexRequest.OpType

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Future, Promise}
import scala.util.{Failure, Success}

abstract class RepositoryOnElasticsearch[ID <: Identifier[String, String], E <: Entity[ID]]
(
  protected val elasticClient: ElasticClient,
  protected val indexName: String,
  protected val typeName: String
)
  extends Repository[ID, E]
    with Translator[ID, E] {

  override def add(entity: E): Future[Unit] = {
    val p = Promise[Unit]()
    try {
      val ret =
        elasticClient.execute(
          index into indexName / typeName id entity.id.id parent entity.id.parent opType OpType.CREATE fields toFieldsFromEntity(entity)
        )

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

  override def exists(id: ID): Future[Boolean] = {
    val p = Promise[Boolean]()
    try {
      elasticClient.execute(
        search in indexName / typeName query {
          termQuery("_id", id.id)
        }
          size 0
          terminateAfter 1
      ) onComplete {
        case Success(r) => p.success(r.totalHits != 0)
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
        update(entity.id.id) in indexName / typeName parent entity.id.parent doc toFieldsFromEntity(entity)
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
        delete(id.id) from indexName / typeName parent id.parent
      ) onComplete {
        case Success(r) =>
          if (r.isFound) p.success(Unit)
          else p.failure(new RepositoryIOException(s"could not delete entity(id:${id.id})."))
        case Failure(e) => p.failure(e)
      }
    } catch {
      case e: Throwable => p.failure(e)
    }
    p.future
  }

  override def resolveById(id: ID): Future[E] = {
    println(s"resolved: $id")
    val p = Promise[E]()
    try {
      elasticClient.execute(
        get id id.id from indexName / typeName parent id.parent
      ) onComplete {
        case Success(r) =>
          if (r.isExists) {
            println(s"resolved: $r")
            p.success(toEntityFromFields(r.getId, r.fieldOpt("_parent").map(_.getValue.toString), r.source))
          }
          else p.failure(new RepositoryIOException(s"could not resolve entity(id:${id.id})."))
        case Failure(e) => p.failure(e)
      }
    } catch {
      case e: Throwable => p.failure(e)
    }
    p.future
  }
}
