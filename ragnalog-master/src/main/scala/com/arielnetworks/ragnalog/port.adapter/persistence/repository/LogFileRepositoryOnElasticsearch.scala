package com.arielnetworks.ragnalog.port.adapter.persistence.repository

import com.arielnetworks.ragnalog.domain.model.archive.ArchiveId
import com.arielnetworks.ragnalog.domain.model.common.RepositoryIOException
import com.arielnetworks.ragnalog.domain.model.logfile.{LogFile, LogFileId, LogFileRepository}
import com.arielnetworks.ragnalog.port.adapter.persistence.translator.LogFileTranslator
import com.sksamuel.elastic4s.{BoolQueryDefinition, ElasticClient, QueryDefinition}
import com.sksamuel.elastic4s.ElasticDsl._
import org.elasticsearch.action.index.IndexRequest.OpType

import scala.collection.mutable.ListBuffer
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Future, Promise}
import scala.util.{Failure, Success}

class LogFileRepositoryOnElasticsearch(elasticClient: ElasticClient, indexName: String = ".ragnalog2")
  extends RepositoryOnElasticsearch[LogFileId, LogFile](elasticClient, indexName, "logFile")
    with LogFileRepository
    with LogFileTranslator {
  override def countRegisteredLogFilesByType(fileType: String, parent: ArchiveId): Future[Long] = ???

  private def buildQuery(containerId: Option[String], archiveId: Option[String], status: Option[String], name: Option[String]): BoolQueryDefinition = {
    val queries = ListBuffer.empty[QueryDefinition]
    containerId.foreach(id => queries += termQuery("containerId", id))
    archiveId.foreach(id => queries += termQuery("_routing", id))
    status.foreach(s => queries += termQuery("status", s))
    name.foreach(n => queries += wildcardQuery("logName", s"*$n*"))
    bool(must(queries))
  }

  override def countAll(containerId: Option[String], archiveId: Option[String], status: Option[String], name: Option[String]): Future[Long] = {

    val p = Promise[Long]()
    try {
      elasticClient.execute(
        search in indexName / typeName query {
          buildQuery(containerId, archiveId, status, name)
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


  override def searchAll(start: Int, limit: Int, containerId: Option[String], archiveId: Option[String], status: Option[String], name: Option[String]): Future[Seq[LogFile]] = {

    val p = Promise[Seq[LogFile]]()
    try {
      elasticClient.execute(
        search in indexName / typeName query {
          buildQuery(containerId, archiveId, status, name)
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

  override def searchRegisteredLogFilesByType(start: Int, limit: Int, fileType: String, parent: ArchiveId): Future[Seq[LogFile]] = ???

  override def addAll(entities: Seq[LogFile], parentId: ArchiveId): Future[Unit] = {
    val p = Promise[Unit]()
    try {
      val ret =
        elasticClient.execute(
          bulk(
            entities.map(entity => {
              index into indexName / typeName id entity.id.id parent parentId.id opType OpType.CREATE fields toFieldsFromEntity(entity)
            })
          )
        )

      ret onComplete {
        case Success(r) =>
          if (!r.hasFailures) p.success(Unit)
          else p.failure(new RepositoryIOException("could not create entity."))
        case Failure(e) => p.failure(e)
      }
    } catch {
      case e: Throwable => p.failure(e)
    }
    p.future
  }

  override def saveAll(entities: Seq[LogFile]): Future[Unit] = {
    println(s"saveAll: $entities")
    val p = Promise[Unit]()
    try {
      val ret =
        elasticClient.execute(
          bulk(
            entities.map(entity => {
              index into indexName / typeName id entity.id.id parent entity.id.parent opType OpType.INDEX fields toFieldsFromEntity(entity)
            })
          )
        )

      ret onComplete {
        case Success(r) =>
          if (!r.hasFailures) p.success(Unit)
          else p.failure(new RepositoryIOException("could not create entity."))
        case Failure(e) => p.failure(e)
      }
    } catch {
      case e: Throwable => p.failure(e)
    }
    p.future
  }
}
