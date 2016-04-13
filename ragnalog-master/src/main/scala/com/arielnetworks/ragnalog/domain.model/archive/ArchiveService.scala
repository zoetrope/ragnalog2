package com.arielnetworks.ragnalog.domain.model.archive

import org.joda.time.DateTime

import scala.concurrent.Future

class ArchiveService(archiveRepository: ArchiveRepository) {

  def createArchive
  (
    id: ArchiveId,
    fileName: String,
    filePath: String,
    archiveType: ArchiveType,
    status: String,
    size: Long,
    uploadedDate: DateTime,
    modifiedDate: DateTime
  ): Future[Archive] = {
    val archive = new Archive(id, fileName, filePath, archiveType, status, size, uploadedDate, modifiedDate, List.empty[LogFile])
    archiveRepository.add(archive).map(_ => archive)

    val logFiles =

  }
}
