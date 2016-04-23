package com.arielnetworks.ragnalog.application.archive.data

import com.arielnetworks.ragnalog.domain.model.archive.LogFile

case class UpdateFileRequest
(
  containerId: String,
  archiveId: String,
  file: LogFile
)
