package com.arielnetworks.ragnalog.application.logfile.data

case class RegisterLogFileRequest
(
  logFileId: String,
  archiveId: String,
  containerId: String,
  logType: String,
  extra: Option[String]
)

case class RegisterLogFileResponse
(
  message: String
)

