package com.arielnetworks.ragnalog.application.archive.data

case class ArchiveResponse
(
  id: String,
  containerId: String,
  fileName: String,
  filePath: String,
  size: Long,
  uploadedDate: String,
  modifiedDate: String,
  fileNameEncoding: String
)

case class GetArchivesResponse
(
  archives: Seq[ArchiveResponse]
)
