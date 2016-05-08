package com.arielnetworks.ragnalog.domain.model.container

import com.arielnetworks.ragnalog.domain.model.common.{EmptyId, Repository}

import scala.concurrent.Future

trait ContainerRepository extends Repository[ContainerId, Container] {

  def countByStatus(isActive: ContainerStatus.Value): Future[Long]

  def searchByStatus(start: Int, limit: Int, status: ContainerStatus.Value): Future[Seq[Container]]

}
