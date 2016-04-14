package com.arielnetworks.ragnalog.port.adapter.persistence.repository

import com.arielnetworks.ragnalog.domain.model.archive.{Archive, ArchiveId, ArchiveRepository}
import com.arielnetworks.ragnalog.port.adapter.persistence.translator.ArchiveTranslator
import com.sksamuel.elastic4s.ElasticClient

import scala.concurrent.Future

class ArchiveRepositoryOnElasticsearch(elasticClient: ElasticClient)
  extends RepositoryOnElasticsearch[ArchiveId, Archive](elasticClient, ".ragnalog2", "archive")
    with ArchiveRepository
    with ArchiveTranslator {

  override def count(): Future[Long] = ???

  override def allArchives(): Future[Seq[Archive]] = ???
}
