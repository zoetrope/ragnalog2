package com.arielnetworks.ragnalog.domain.model.archive

import org.joda.time.DateTime

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

class ArchiveService(archiveRepository: ArchiveRepository) {

  def createArchive
  (
    id: ArchiveId,
    fileName: String,
    filePath: String,
    archiveType: ArchiveType,
    size: Long,
    uploadedDate: DateTime,
    modifiedDate: DateTime
  ): Future[Archive] = {
    val archive = new Archive(id, fileName, filePath, archiveType, size, uploadedDate, modifiedDate, List.empty[LogFile])
    archiveRepository.add(archive).map(_ => archive)

    //    val logFiles =

  }
}
