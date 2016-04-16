package com.arielnetworks.ragnalog.application

case class RegistrationRequest
(
  containerId: String,
  archiveId: String,
  archiveFileName: String,
  filePath: String,
  logType: String,
  extra: String,
  indexName: String,
  priority: Int
)
