package com.arielnetworks.ragnalog.domain.model.logfile

import com.arielnetworks.ragnalog.domain.model.archive.ArchiveId
import com.arielnetworks.ragnalog.domain.model.common.Repository

import scala.concurrent.Future

trait LogFileRepository extends Repository[LogFileId, LogFile, ArchiveId] {

  def countRegisteredLogFilesByType(fileType: String, parent: ArchiveId): Future[Long]

  def searchRegisteredLogFilesByType(start: Int, limit: Int, fileType: String, parent: ArchiveId): Future[Seq[LogFile]]

  def countAll(containerId: Option[String], archiveId: Option[String], status: Option[String], name: Option[String]): Future[Long]

  def searchAll(start: Int, limit: Int, containerId: Option[String], archiveId: Option[String], status: Option[String], name: Option[String]): Future[Seq[LogFile]]

  def addAll(entities: Seq[LogFile], parentId: ArchiveId): Future[Unit]
}
