package com.arielnetworks.ragnalog.domain.model.archive

import com.arielnetworks.ragnalog.domain.model.common.{Entity, Identifier}
import org.joda.time.DateTime

import scala.concurrent.Future
import scalax.file.Path

case class ArchiveId(value: String) extends Identifier[String]

case class Archive
(
  id: ArchiveId,
  fileName: String,
  filePath: Path,
  archiveType: ArchiveType,
  size: Long,
  uploadedDate: DateTime,
  modifiedDate: DateTime
) extends Entity[ArchiveId] {

  def extractLogFiles() : Future[Seq[LogFile]] = ???
}

