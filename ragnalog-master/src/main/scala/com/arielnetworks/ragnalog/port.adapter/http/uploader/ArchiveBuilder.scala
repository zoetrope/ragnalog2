package com.arielnetworks.ragnalog.port.adapter.http.uploader

import java.io.{File, FileInputStream, FileOutputStream}

import com.arielnetworks.ragnalog.support.LoanSupport

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
  currentChunkSize: Int
)

class ArchiveBuilder(val containerId: String, val identifier: String) extends LoanSupport {

  val chunks = mutable.ListBuffer[ArchiveChunk]()

  //TODO: error handling
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
          val allChunkWasUploaded = chunks.size == c.totalChunks
          if (allChunkWasUploaded) {
            concatenate()
          }
          allChunkWasUploaded
        }
      case None => false
    }
  }

  private def concatenate(): Unit = {

    println(s"concatenate files: $chunks")

    val fileName = chunks.head.filename

    val filePath = Path("/", "tmp", containerId, fileName) //TODO
    filePath.deleteIfExists()
    filePath.createFile()
    val dest = filePath match {
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

  }
}
