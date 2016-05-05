package com.arielnetworks.ragnalog.port.adapter.persistence.repository

import com.arielnetworks.ragnalog.domain.model.archive.ArchiveId
import com.arielnetworks.ragnalog.domain.model.logfile.{LogFile, LogFileId, LogFileRepository}
import com.arielnetworks.ragnalog.port.adapter.persistence.translator.LogFileTranslator
import com.sksamuel.elastic4s.ElasticClient

import scala.concurrent.Future

class LogFileRepositoryOnElasticsearch(elasticClient: ElasticClient, indexName: String = ".ragnalog2")
  extends RepositoryOnElasticsearch[LogFileId, LogFile, ArchiveId](elasticClient, indexName, "logFile")
    with LogFileRepository
    with LogFileTranslator {
  override def countRegisteredLogFilesByType(fileType: String, parent: ArchiveId): Future[Long] = ???

  override def searchAll(start: Int, limit: Int, parent: ArchiveId): Future[Seq[LogFile]] = ???

  override def countAll(parent: ArchiveId): Future[Long] = ???

  override def searchRegisteredLogFilesByType(start: Int, limit: Int, fileType: String, parent: ArchiveId): Future[Seq[LogFile]] = ???
}
