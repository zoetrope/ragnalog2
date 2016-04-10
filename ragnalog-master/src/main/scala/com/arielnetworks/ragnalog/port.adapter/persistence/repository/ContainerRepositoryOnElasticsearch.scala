package com.arielnetworks.ragnalog.port.adapter.persistence.repository

import com.arielnetworks.ragnalog.domain.model.common.RepositoryIOException
import com.arielnetworks.ragnalog.domain.model.container.{Container, ContainerId, ContainerRepository, ContainerStatus}
import com.sksamuel.elastic4s.ElasticClient
import com.sksamuel.elastic4s.ElasticDsl._

import scala.concurrent.{Future, Promise}
import scala.util.{Failure, Success}

class ContainerRepositoryOnElasticsearch(elasticClient: ElasticClient)
  extends RepositoryOnElasticsearch[ContainerId, Container](elasticClient, ".ragnalog2", "container")
    with ContainerRepository
    with ContainerTranslator {

  def countByStatus(status: ContainerStatus.Value): Future[Long] = ???

  def searchByStatus(from: Long, size: Long, status: ContainerStatus.Value): Future[Seq[Container]] = ???

}
