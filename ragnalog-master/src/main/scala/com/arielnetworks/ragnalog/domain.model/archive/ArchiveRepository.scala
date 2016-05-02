package com.arielnetworks.ragnalog.domain.model.archive

import com.arielnetworks.ragnalog.domain.model.common.Repository
import com.arielnetworks.ragnalog.domain.model.container.ContainerId

import scala.concurrent.Future

trait ArchiveRepository extends Repository[ArchiveId, Archive, ContainerId] {

  def count(parent: ContainerId): Future[Long]

  def allArchives(start: Int, limit: Int, parent: ContainerId): Future[Seq[Archive]];

}
