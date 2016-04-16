package com.arielnetworks.ragnalog.domain.model.archive

import com.arielnetworks.ragnalog.domain.model.common.{Entity, Identifier}
import org.joda.time.DateTime

case class ArchiveId(value: String) extends Identifier[String]

case class Archive
(
  id: ArchiveId,
  fileName: String,
  filePath: String,
  archiveType: ArchiveType,
  size: Long,
  uploadedDate: DateTime,
  modifiedDate: DateTime,
  logFiles: List[LogFile]
) extends Entity[ArchiveId] {
  def addLogFile() = ???
}

