package com.arielnetworks.ragnalog.application.logfile.data

case class RegisterLogFileRequest
(
  id: String,
  fileType: String,
  extra: Option[String]
)
