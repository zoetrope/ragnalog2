package com.arielnetworks.ragnalog.domain.model.archive

import com.arielnetworks.ragnalog.domain.model.common.{Entity, Identifier}
import com.arielnetworks.ragnalog.domain.model.logfile.LogFile

sealed abstract class ArchiveType

case object None extends ArchiveType

case object Zip extends ArchiveType

case object Tgz extends ArchiveType

case object Tar extends ArchiveType

case object GZip extends ArchiveType

case class ArchiveId(value: String) extends Identifier[String]

case class Archive
(
  id: ArchiveId,
  fileName: String,
  filePath: String,
  archiveType: ArchiveType,
  status: String,
  size: Long,
  uploadedDate: String,
  modifiedDate: String,
  files: Seq[LogFile]
) extends Entity[ArchiveId]

