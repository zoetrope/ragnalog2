package com.arielnetworks.ragnalog.port.adapter.http.uploader

import scalax.file.Path
import java.io.{File, FileInputStream, FileOutputStream}
import java.util.concurrent.ConcurrentHashMap

import scala.collection.mutable
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
  currentChunkSize: Int
)

class ArchiveUploader(filePath: Path) {

  val chunks = mutable.ListBuffer[ArchiveChunk]()

  def append(parts: Map[String, Any]): Boolean = {

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
    } yield {
      ArchiveChunk(file, totalSize, relativePath, identifier, chunkNumber, totalChunks, filename, chunkSize, currentChunkSize)
    }

    chunk match {
      case Some(c) =>
        chunks.synchronized {
          chunks += c
          val allChunksUploaded = chunks.size == c.totalChunks
          if (allChunksUploaded) {
            concatenate()
          }
          allChunksUploaded
        }
      case None => false
    }
  }

  private def concatenate(): Unit = {

    println(s"concatenate files: $chunks")

    filePath.deleteIfExists()
    filePath.createFile()
    val dest = filePath match {
      case x: DefaultPath => x.jfile
    }
    val output = new FileOutputStream(dest)

    chunks.sortBy(_.chunkNumber).foreach[Unit](chunk => {
      println(s"transfer: $chunk")
      val input = new FileInputStream(chunk.file)
      input.getChannel.transferTo(0, input.getChannel.size(), output.getChannel)
      input.close()
      chunk.file.delete()
    })

    output.close()
  }
}
