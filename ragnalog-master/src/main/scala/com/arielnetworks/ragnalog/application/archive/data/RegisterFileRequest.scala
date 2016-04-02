package com.arielnetworks.ragnalog.application.archive.data

case class RegisterFileRequest
(
  containerId: String,
  archiveId: String,
  archiveFileName: String,
  filePath: String,
  logType: String,
  extra: String,
  priority: Int)
