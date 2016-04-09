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
  extends Repository[ID, E] {

  import ExecutionContext.Implicits.global

  override def add(entity: E): Future[Either[AdditionError, Unit]] = {

    val p = Promise[Either[AdditionError, Unit]]()

    try {
      val f = elasticClient.execute(
        index into indexName / typeName id entity.id.value opType OpType.CREATE fields toFieldsFromEntity(entity)
      )

      f.onComplete {
        case Success(r) => p.success(Right())
        case Failure(e) =>{
          println("*******************" + e.getMessage)
          println(e.getCause.getMessage)
          println(e.getCause.getCause.getMessage)
          p.success(Left(new AlreadyExists))
        }
      }
    } catch {
      case e: Throwable => p.success(Left(new InfrastructureError(e)))
    }

    return p.future

  }

  override def update(value: E): Future[ID] = {
    add(value) onSuccess {
      case Right(_) =>
      case Left(e) => e match {
        case AlreadyExists() =>
        case InfrastructureError(_) =>
      }
    }

    return Future.successful(value.id)
  }

  override def deleteById(id: ID): Future[E] = ???

  override def resolveById(id: ID): Future[E] = ???

  protected def toFieldsFromEntity(entity: E): Iterable[(String, Any)] = {
    return List("a" -> "", "" -> "")
  }
}
