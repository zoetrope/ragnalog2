package com.arielnetworks.ragnalog.port.adapter.persistence.repository

import com.arielnetworks.ragnalog.domain.model.container.{Container, ContainerId, ContainerRepository, ContainerStatus}
import com.arielnetworks.ragnalog.port.adapter.persistence.translator.ContainerTranslator
import com.sksamuel.elastic4s.ElasticClient
import com.sksamuel.elastic4s.ElasticDsl._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Future, Promise}
import scala.util.{Failure, Success}

class ContainerRepositoryOnElasticsearch(elasticClient: ElasticClient, indexName: String = ".ragnalog2")
  extends RepositoryOnElasticsearch[ContainerId, Container](elasticClient, indexName, "container")
    with ContainerRepository
    with ContainerTranslator {

  def countByStatus(status: ContainerStatus.Value): Future[Long] = {
    val p = Promise[Long]()
    try {
      elasticClient.execute(
        search in indexName / typeName query {
          termQuery("status", status.toString)
        }
          size 0
      ) onComplete {
        case Success(r) => p.success(r.totalHits)
        case Failure(e) => p.failure(e)
      }
    } catch {
      case e: Throwable => p.failure(e)
    }
    p.future
  }

  def searchByStatus(start: Int, limit: Int, status: ContainerStatus.Value): Future[Seq[Container]] = {
    val p = Promise[Seq[Container]]()
    try {
      elasticClient.execute(
        search in indexName / typeName query {
          termQuery("status", status.toString)
        }
          from start
          size limit
      ) onComplete {
        case Success(r) => p.success(r.hits.map(hit => toEntityFromFields(hit.getId, null, hit.getSource)))
        case Failure(e) => p.failure(e)
      }
    } catch {
      case e: Throwable => p.failure(e)
    }
    p.future
  }

}
