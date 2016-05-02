package com.arielnetworks.ragnalog.application.archive.data

case class ArchiveResponse
(
  id: String,
  fileName: String,
  filePath: String,
  archiveType: String,
  size: Long,
  uploadedDate: String,
  modifiedDate: String
)

case class GetArchivesResponse
(
  archives: Seq[ArchiveResponse]
)
