package com.arielnetworks.ragnalog.application.logfile.data

case class RegisterLogFileRequest
(
  id: String,
  archiveId: String,
  logType: String,
  extra: Option[String]
)

case class RegisterLogFileResponse
(
  message: String
)

