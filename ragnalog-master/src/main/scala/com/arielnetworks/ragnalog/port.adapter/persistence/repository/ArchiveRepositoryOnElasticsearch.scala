package com.arielnetworks.ragnalog.port.adapter.persistence.repository

import com.arielnetworks.ragnalog.domain.model.archive.{Archive, ArchiveId, ArchiveRepository}
import com.arielnetworks.ragnalog.domain.model.container.ContainerId
import com.arielnetworks.ragnalog.port.adapter.persistence.translator.ArchiveTranslator
import com.sksamuel.elastic4s.ElasticClient
import com.sksamuel.elastic4s.ElasticDsl._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Future, Promise}
import scala.util.{Failure, Success}

class ArchiveRepositoryOnElasticsearch(elasticClient: ElasticClient, indexName: String = ".ragnalog2")
  extends RepositoryOnElasticsearch[ArchiveId, Archive](elasticClient, indexName, "archive")
    with ArchiveRepository
    with ArchiveTranslator {

  def count(parent: ContainerId): Future[Long] = {
    val p = Promise[Long]()
    try {
      elasticClient.execute(
        search in indexName / typeName query {
          termQuery("_parent", parent.id)
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

  def allArchives(start: Int, limit: Int, parent: ContainerId): Future[Seq[Archive]] = {
    println(s"allArchives: $start, $limit, ${parent.id}")
    val p = Promise[Seq[Archive]]()
    try {
      elasticClient.execute(
        search in indexName / typeName query {
          termQuery("_parent", parent.id)
        }
          from start
          size limit
      ) onComplete {
        case Success(r) => p.success(r.hits.map(hit => toEntityFromFields(hit.getId, hit.fieldOpt("_parent").map(_.getValue[String]).get, hit.getSource)))
        case Failure(e) => p.failure(e)
      }
    } catch {
      case e: Throwable => p.failure(e)
    }
    p.future
  }
}
