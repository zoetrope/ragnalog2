package com.arielnetworks.ragnalog.port.adapter.persistence.repository

import com.arielnetworks.ragnalog.domain.model.archive.ArchiveId
import com.arielnetworks.ragnalog.domain.model.common.RepositoryIOException
import com.arielnetworks.ragnalog.domain.model.logfile.{LogFile, LogFileId, LogFileRepository}
import com.arielnetworks.ragnalog.port.adapter.persistence.translator.LogFileTranslator
import com.sksamuel.elastic4s.ElasticClient
import com.sksamuel.elastic4s.ElasticDsl._
import org.elasticsearch.action.index.IndexRequest.OpType

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Future, Promise}
import scala.util.{Failure, Success}

class LogFileRepositoryOnElasticsearch(elasticClient: ElasticClient, indexName: String = ".ragnalog2")
  extends RepositoryOnElasticsearch[LogFileId, LogFile, ArchiveId](elasticClient, indexName, "logFile")
    with LogFileRepository
    with LogFileTranslator {
  override def countRegisteredLogFilesByType(fileType: String, parent: ArchiveId): Future[Long] = ???

  override def searchAll(start: Int, limit: Int, parent: ArchiveId): Future[Seq[LogFile]] = ???

  override def countAll(parent: ArchiveId): Future[Long] = ???

  override def searchRegisteredLogFilesByType(start: Int, limit: Int, fileType: String, parent: ArchiveId): Future[Seq[LogFile]] = ???

  override def addAll(entities: Seq[LogFile], parentId: ArchiveId): Future[Unit] = {
    val p = Promise[Unit]()
    try {
      val ret =
        elasticClient.execute(
          bulk(
            entities.map(entity => {
              index into indexName / typeName id entity.id.value parent parentId.value opType OpType.CREATE fields toFieldsFromEntity(entity)
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
