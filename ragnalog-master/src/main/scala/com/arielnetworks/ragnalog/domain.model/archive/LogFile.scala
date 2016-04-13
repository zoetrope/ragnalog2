package com.arielnetworks.ragnalog.domain.model.archive

import com.arielnetworks.ragnalog.domain.model.common.{Entity, Identifier}

sealed abstract class LogStatus

case object Uploading extends LogStatus

case object Uploaded extends LogStatus

case object Registering extends LogStatus

case object Registered extends LogStatus

case object Unregistered extends LogStatus

case object Error extends LogStatus

case class LogFileId(value: String) extends Identifier[String]

case class LogFile
(
  id: LogFileId,
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
) extends Entity[LogFileId]
