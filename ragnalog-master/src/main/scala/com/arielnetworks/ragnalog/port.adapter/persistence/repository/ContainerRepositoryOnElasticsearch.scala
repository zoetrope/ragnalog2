package com.arielnetworks.ragnalog.port.adapter.persistence.repository

import com.arielnetworks.ragnalog.domain.model.container.{Container, ContainerId, ContainerRepository}
import com.sksamuel.elastic4s.ElasticClient

class ContainerRepositoryOnElasticsearch(elasticClient: ElasticClient)
  extends RepositoryOnElasticsearch[ContainerId, Container](elasticClient, ".ragnalog2", "container")
    with ContainerRepository {

}
