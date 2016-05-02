package com.arielnetworks.ragnalog.application.archive

import com.arielnetworks.ragnalog.application.archive.data.{ArchiveResponse, GetArchivesResponse}
import com.arielnetworks.ragnalog.domain.model.archive.{ArchiveId, ArchiveType, _}
import com.arielnetworks.ragnalog.domain.model.container.ContainerId
import com.arielnetworks.ragnalog.port.adapter.http.uploader.ArchiveInfo
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success}

class ArchiveService(archiveRepository: ArchiveRepository) {
  def uploadArchiveFile(info: ArchiveInfo) = {

    val lastModified = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parseDateTime(info.lastModified)

    val archive = new Archive(
      ArchiveId(info.identifier),
      info.filename,
      info.uploadedFilePath,
      ArchiveType.fromExtension(info.filename),
      info.totalSize,
      new DateTime(),
      lastModified,
      List.empty[LogFile]
    )
    println(s"createArchive: $archive")

    archiveRepository.add(archive, Some(ContainerId(info.containerId))).map(_ => archive) onComplete {
      case Success(_) => println("archive added")
      case Failure(ex) => println("failed to add archive"); ex.printStackTrace()
    }
  }

  def removeArchiveFile() = ???

  def registerLogFile() = ???

  def unregisterLogFile() = ???

  def archives(containerId: String): Future[GetArchivesResponse] = {
    //TODO: add converter

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

  def logFiles(archiveId: ArchiveId) = ???

}
