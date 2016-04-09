package com.arielnetworks.ragnalog.port.adapter.persistence.repository

import com.arielnetworks.ragnalog.domain.model.container.{Container, ContainerId, ContainerRepository}

class ContainerRepositoryOnElasticsearch
  extends RepositoryOnElasticsearch[ContainerId, Container]
    with ContainerRepository {

}
