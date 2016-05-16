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
      ArchiveId(info.identifier, info.containerId),
      info.filename,
      info.uploadedFilePath,
      info.totalSize,
      new DateTime(),
      lastModified,
      "UTF-8"
    )
    println(s"createArchive: $archive")

    for {
      _ <- archiveRepository.add(archive)
      logFiles = archive.extractLogFiles(ContainerId(info.containerId))
      _ <- logFileService.addLogFiles(logFiles, archive.id)
    } yield ()
  }

  def removeArchive(archiveId: ArchiveId): Future[ArchiveResponse] = {
    println(s"delete archive: $archiveId")
    for {
    //TODO: unregister all logfiles
    //      _ <- logFileService.removeAll(archiveId)
      archive <- archiveRepository.resolveById(archiveId)
      //TODO: delete uploaded file
      //      _ = archive.remove()
      _ <- archiveRepository.deleteById(archiveId)
    } yield new ArchiveResponse(
      archive.id.id,
      archive.id.parent,
      archive.fileName,
      archive.filePath.path,
      archive.size,
      archive.uploadedDate.toString(),
      archive.modifiedDate.toString(),
      archive.fileNameEncoding)
  }

  def archives(containerId: ContainerId): Future[GetArchivesResponse] = {

    val list = for {
      count <- archiveRepository.count(containerId)
      archives <- archiveRepository.allArchives(0, count.asInstanceOf[Int], containerId)
    } yield archives

    list.map(list => new GetArchivesResponse(
      list.map(a => new ArchiveResponse(
        a.id.id,
        a.id.parent,
        a.fileName,
        a.filePath.path,
        a.size,
        a.uploadedDate.toString,
        a.modifiedDate.toString(),
        a.fileNameEncoding
      ))
    ))
  }

  def removeAll(containerId: ContainerId): Future[Unit] = ???

  def activateAll(containerId: ContainerId): Future[Unit] = ???

  def deactivateAll(containerId: ContainerId): Future[Unit] = ???

}
