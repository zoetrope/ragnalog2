package com.arielnetworks.ragnalog.domain.model.archive

sealed abstract class LogStatus

case object Uploading extends LogStatus

case object Uploaded extends LogStatus

case object Registering extends LogStatus

case object Registered extends LogStatus

case object Unregistered extends LogStatus

case object Error extends LogStatus

case class LogFile
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
