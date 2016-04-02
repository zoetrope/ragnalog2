package com.arielnetworks.ragnalog.domain.model.archive

import com.arielnetworks.ragnalog.domain.model.common.{Entity, Identifier}

sealed abstract class ArchiveType
case object None extends ArchiveType
case object Zip extends ArchiveType
case object Tgz extends ArchiveType
case object Tar extends ArchiveType
case object GZip extends ArchiveType

sealed abstract class LogStatus
case object Uploading extends LogStatus
case object Uploaded extends LogStatus
case object Registering extends LogStatus
case object Registered extends LogStatus
case object Unregistered extends LogStatus
case object Error extends LogStatus


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
  files: Seq[ArchiveFile]
) extends Entity[ArchiveId]

case class ArchiveFile
(
  filePath: String,
  fileType: String,
  status: LogStatus,
  index: String,
  from: String,
  to: String,
  extra: String,
  count: Long,
  errorCount: Long,
  errorMessage: String
)
