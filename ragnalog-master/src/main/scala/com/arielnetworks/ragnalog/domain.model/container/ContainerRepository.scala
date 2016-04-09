package com.arielnetworks.ragnalog.domain.model.container

import com.arielnetworks.ragnalog.domain.model.common.{EntityIOContext, Repository}

trait ContainerRepository extends Repository[ContainerId, Container, EntityIOContext] {

}
