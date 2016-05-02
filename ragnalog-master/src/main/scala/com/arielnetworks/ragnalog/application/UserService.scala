package com.arielnetworks.ragnalog.application

import com.arielnetworks.ragnalog.domain.model.archive.{ArchiveId, ArchiveService, ArchiveType}
import com.arielnetworks.ragnalog.domain.model.container.ContainerId
import com.arielnetworks.ragnalog.port.adapter.http.uploader.ArchiveInfo
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}

class UserService
(
  archiveService: ArchiveService
) {

  def uploadArchiveFile(info: ArchiveInfo) = {

    val lastModified = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parseDateTime(info.lastModified)

    archiveService.createArchive(
      ContainerId(info.containerId),
      ArchiveId(info.identifier),
      info.filename,
      info.uploadedFilePath,
      ArchiveType.fromExtension(info.filename),
      info.totalSize,
      new DateTime(),
      lastModified
    ) onComplete {
      case Success(_) => println("archive added")
      case Failure(ex) => println("failed to add archive"); ex.printStackTrace()
    }
  }

  def removeArchiveFile() = ???

  def registerLogFile() = ???

  def unregisterLogFile() = ???

  def archives(containerId: ContainerId) = ???

  def logFiles(archiveId: ArchiveId) = ???
}
