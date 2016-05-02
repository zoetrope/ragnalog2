package com.arielnetworks.ragnalog.domain.model.archive

import com.arielnetworks.ragnalog.domain.model.container.ContainerId
import org.joda.time.DateTime

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scalax.file.Path

class ArchiveService(archiveRepository: ArchiveRepository) {

  def createArchive
  (
    parentId: ContainerId,
    id: ArchiveId,
    fileName: String,
    filePath: Path,
    archiveType: ArchiveType,
    size: Long,
    uploadedDate: DateTime,
    modifiedDate: DateTime
  ): Future[Archive] = {

    val archive = new Archive(id, fileName, filePath, archiveType, size, uploadedDate, modifiedDate, List.empty[LogFile])
    println(s"createArchive: $archive")
    archiveRepository.add(archive, Some(parentId)).map(_ => archive)

    //    val logFiles =

  }

  def archives(parent: ContainerId): Future[Seq[Archive]] = {
    for {
      count <- archiveRepository.count(parent)
      archives <- archiveRepository.allArchives(0, count.asInstanceOf[Int], parent)
    } yield archives
  }
}
