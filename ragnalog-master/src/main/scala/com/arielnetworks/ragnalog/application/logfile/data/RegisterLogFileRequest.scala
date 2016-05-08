package com.arielnetworks.ragnalog.application.logfile.data

case class RegisterLogFileRequest
(
  id: String,
  logType: String,
  extra: Option[String]
)

