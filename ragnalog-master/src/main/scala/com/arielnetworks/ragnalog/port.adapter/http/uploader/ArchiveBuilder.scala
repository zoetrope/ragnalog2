package com.arielnetworks.ragnalog.port.adapter.http.uploader

import java.io.{File, FileInputStream, FileOutputStream}

import com.arielnetworks.ragnalog.support.LoanSupport
import com.typesafe.config.ConfigFactory

import scala.collection.mutable
import scalax.file.Path
import scalax.file.defaultfs.DefaultPath

case class ArchiveChunk
(
  file: File,
  totalSize: Long,
  relativePath: String,
  identifier: String,
  chunkNumber: Int,
  totalChunks: Int,
  filename: String,
  chunkSize: Int,
  currentChunkSize: Int,
  lastModified: String
)

case class ArchiveInfo
(
  containerId: String,
  uploadedFilePath: Path,
  totalSize: Long,
  identifier: String,
  filename: String,
  lastModified: String
)

class ArchiveBuilder(val containerId: String, val identifier: String) extends LoanSupport {

  val chunks = mutable.ListBuffer[ArchiveChunk]()
  val config = ConfigFactory.load().getConfig("ragnalog-master.uploader")
  val uploadedFilesDirectory = Path(config.getString("uploaded-files-directory"), '/')

  //TODO: error handling
  def append(parts: Map[String, Any]): Option[ArchiveInfo] = {

    val chunk = for {
      file <- parts.get("file").map(_.asInstanceOf[File])
      totalSize <- parts.get("flowTotalSize").map(_.asInstanceOf[String].toLong)
      relativePath <- parts.get("flowRelativePath").map(_.asInstanceOf[String])
      identifier <- parts.get("flowIdentifier").map(_.asInstanceOf[String])
      chunkNumber <- parts.get("flowChunkNumber").map(_.asInstanceOf[String].toInt)
      totalChunks <- parts.get("flowTotalChunks").map(_.asInstanceOf[String].toInt)
      filename <- parts.get("flowFilename").map(_.asInstanceOf[String])
      chunkSize <- parts.get("flowChunkSize").map(_.asInstanceOf[String].toInt)
      currentChunkSize <- parts.get("flowCurrentChunkSize").map(_.asInstanceOf[String].toInt)
      lastModified <- parts.get("lastModified").map(_.asInstanceOf[String])
    } yield {
      ArchiveChunk(file, totalSize, relativePath, identifier, chunkNumber, totalChunks, filename, chunkSize, currentChunkSize, lastModified)
    }

    chunk match {
      case Some(c) =>
        chunks.synchronized {
          chunks += c
          val allChunkWasUploaded = chunks.size == c.totalChunks
          if (allChunkWasUploaded) {
            val path = concatenate()
            Some(ArchiveInfo(containerId, path, c.totalSize, c.identifier, c.filename, c.lastModified))
          } else {
            None
          }
        }
      case None => None
    }
  }

  private def concatenate(): Path = {

    println(s"concatenate files: $chunks")

    val archiveName = chunks.head.filename

    val archivePath = uploadedFilesDirectory / Path(containerId, archiveName)
    archivePath.deleteIfExists()
    archivePath.createFile()
    val dest = archivePath match {
      case x: DefaultPath => x.jfile
    }

    using(new FileOutputStream(dest)) { output =>
      chunks.sortBy(_.chunkNumber).foreach[Unit](chunk => {
        println(s"transfer: $chunk")
        using(new FileInputStream(chunk.file)) { input =>
          input.getChannel.transferTo(0, input.getChannel.size(), output.getChannel)
        }
        chunk.file.delete()
      })
    }

    archivePath
  }

  def allChunkWasUploaded: Boolean = ???

  def uploadedArchivePath: Option[Path] = ???
}
