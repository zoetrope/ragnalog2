package com.arielnetworks.ragnalog.test

import com.arielnetworks.ragnalog.domain.model.container.ContainerStatus
import com.arielnetworks.ragnalog.port.adapter.persistence.repository.ContainerRepositoryOnElasticsearch
import com.sksamuel.elastic4s.ElasticClient
import com.sksamuel.elastic4s.ElasticDsl._

import scala.concurrent.Future

trait TestSupport {

  def clearIndex(elasticClient: ElasticClient) = {

//    val repo = new ContainerRepositoryOnElasticsearch(elasticClient)
//
//    for {
//      count <- repo.countByStatus(ContainerStatus.Active)
//      containers <- repo.searchByStatus(0, count.asInstanceOf[Int], ContainerStatus.Active)
//      containers.map(container => repo.deleteById(container.id))
//      Future.sequence()
//    }
  }
}
