package com.arielnetworks.ragnalog.application

import com.arielnetworks.ragnalog.domain.model.archive.{ArchiveId, ArchiveService}
import com.arielnetworks.ragnalog.domain.model.container.ContainerId

class UserService
(
  archiveService: ArchiveService
) {

  def uploadArchiveFile
  (
    fileName: String,
    filePath: String,
    archiveType: String,
    status: String,
    size: Long,
    uploadedDate: String,
    modifiedDate: String
  ) = {

    archiveService.createArchive()
  }

  def removeArchiveFile() = ???

  def registerLogFile() = ???

  def unregisterLogFile() = ???

  def archives(containerId: ContainerId) = ???

  def logFiles(archiveId: ArchiveId) = ???
}
