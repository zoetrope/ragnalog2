package com.arielnetworks.ragnalog.application.archive.data

case class AddArchiveRequest
(
  containerId: String,
  uniqueId: String,
  fileName: String,
  filePath: String,
  lastModified: String,
  size: Long
)
