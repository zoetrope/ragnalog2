package com.arielnetworks.ragnalog.application.archive

import com.arielnetworks.ragnalog.application.archive.data.{ArchiveResponse, GetArchivesResponse}
import com.arielnetworks.ragnalog.application.logfile.LogFileService
import com.arielnetworks.ragnalog.domain.model.archive.{ArchiveId, ArchiveType, _}
import com.arielnetworks.ragnalog.domain.model.container.ContainerId
import com.arielnetworks.ragnalog.port.adapter.http.uploader.ArchiveInfo
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class ArchiveService
(
  archiveRepository: ArchiveRepository,
  logFileService: LogFileService
) {
  def registerArchive(info: ArchiveInfo) = {

    val lastModified = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parseDateTime(info.lastModified)

    val archive = new Archive(
      ArchiveId(info.identifier),
      info.filename,
      info.uploadedFilePath,
      ArchiveType.fromExtension(info.filename),
      info.totalSize,
      new DateTime(),
      lastModified
    )
    println(s"createArchive: $archive")

    for {
      _ <- archiveRepository.add(archive, Some(ContainerId(info.containerId)))
      logFiles <- archive.extractLogFiles()
      _ <- logFileService.addLogFiles(logFiles)
    } yield ()
  }

  def removeArchive(id: String) = {
    val archiveId = ArchiveId(id)
    for {
      _ <- logFileService.removeAll(id)
      archive <- archiveRepository.resolveById(archiveId)
      _ = archive.remove()
      _ <- archiveRepository.deleteById(archiveId)
    } yield ()
  }

  def archives(containerId: String): Future[GetArchivesResponse] = {

    val list = for {
      count <- archiveRepository.count(ContainerId(containerId))
      archives <- archiveRepository.allArchives(0, count.asInstanceOf[Int], ContainerId(containerId))
    } yield archives

    list.map(list => new GetArchivesResponse(
      list.map(a => new ArchiveResponse(
        a.id.value,
        a.fileName,
        a.filePath.path,
        a.archiveType.toString,
        a.size,
        a.uploadedDate.toString,
        a.modifiedDate.toString()
      ))
    ))
  }

  def removeAll(containerId: String): Future[Unit] = ???

  def activateAll(containerId: ContainerId): Future[Unit] = ???

  def deactivateAll(containerId: ContainerId): Future[Unit] = ???

}
