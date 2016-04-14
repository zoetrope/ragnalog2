package com.arielnetworks.ragnalog.domain.model.archive

import com.arielnetworks.ragnalog.domain.model.common.Repository

import scala.concurrent.Future

trait ArchiveRepository extends Repository[ArchiveId, Archive] {

  def count(): Future[Long]

  def allArchives(): Future[Seq[Archive]]

}
