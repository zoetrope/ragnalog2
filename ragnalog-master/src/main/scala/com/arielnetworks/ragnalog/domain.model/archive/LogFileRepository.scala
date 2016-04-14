package com.arielnetworks.ragnalog.domain.model.archive

import com.arielnetworks.ragnalog.domain.model.common.Repository

import scala.concurrent.Future

trait LogFileRepository extends Repository[LogFileId, LogFile] {

  def searchRegisteredLogFilesByType(fileType: String): Future[LogFile]

}
