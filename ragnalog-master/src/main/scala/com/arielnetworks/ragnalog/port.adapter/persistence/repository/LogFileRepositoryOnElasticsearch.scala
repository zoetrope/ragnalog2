package com.arielnetworks.ragnalog.port.adapter.persistence.repository

import com.arielnetworks.ragnalog.domain.model.archive.{LogFile, LogFileId, LogFileRepository}
import com.arielnetworks.ragnalog.port.adapter.persistence.translator.LogFileTranslator
import com.sksamuel.elastic4s.ElasticClient

import scala.concurrent.Future

class LogFileRepositoryOnElasticsearch(elasticClient: ElasticClient, indexName: String = ".ragnalog2")
  extends RepositoryOnElasticsearch[LogFileId, LogFile](elasticClient, indexName, "logFile")
    with LogFileRepository
    with LogFileTranslator {

  override def searchRegisteredLogFilesByType(fileType: String): Future[LogFile] = ???
}
