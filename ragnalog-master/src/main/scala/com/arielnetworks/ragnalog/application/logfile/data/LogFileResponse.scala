package com.arielnetworks.ragnalog.application.logfile.data

case class LogFileResponse
(
  id: String,
  archiveId: String,
  archiveName: String,
  logName: String,
  logType: Option[String],
  status: String,
  indexName: Option[String],
  from: Option[String],
  to: Option[String],
  extra: Option[String],
  count: Option[Long]
)

case class GetLogFilesResponse
(
  logFiles: Seq[LogFileResponse],
  totalCount: Long,
  currentPage: Int
)
