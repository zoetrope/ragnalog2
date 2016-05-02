package com.arielnetworks.ragnalog.domain.model.archive

import com.arielnetworks.ragnalog.domain.model.common.Repository
import com.arielnetworks.ragnalog.domain.model.container.ContainerId

import scala.concurrent.Future

trait ArchiveRepository extends Repository[ArchiveId, Archive, ContainerId] {

  def count(): Future[Long]

  def allArchives(): Future[Seq[Archive]]

}
