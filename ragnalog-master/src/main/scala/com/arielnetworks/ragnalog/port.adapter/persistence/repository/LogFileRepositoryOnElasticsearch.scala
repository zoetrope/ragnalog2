package com.arielnetworks.ragnalog.port.adapter.persistence.repository

import com.arielnetworks.ragnalog.domain.model.archive.{LogFile, LogFileId, LogFileRepository}
import com.arielnetworks.ragnalog.port.adapter.persistence.translator.LogFileTranslator
import com.sksamuel.elastic4s.ElasticClient

import scala.concurrent.Future

class LogFileRepositoryOnElasticsearch(elasticClient: ElasticClient)
  extends RepositoryOnElasticsearch[LogFileId, LogFile](elasticClient, ".ragnalog2", "logFile")
    with LogFileRepository
    with LogFileTranslator {

  override def searchRegisteredLogFilesByType(fileType: String): Future[LogFile] = ???
}
