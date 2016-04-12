package com.arielnetworks.ragnalog.support

import java.io.{File, FileInputStream, InputStream}
import java.nio.file.Paths
import java.util.zip.GZIPInputStream

import com.arielnetworks.ragnalog.domain.model.archive.ArchiveType
import org.apache.commons.compress.archivers.ArchiveStreamFactory

object ArchiveUtil {

  private def getFileListRecursive(basedir: String, archiverName: String, is: InputStream): Seq[String] = {
    val archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(archiverName, is)

    Iterator.continually(archiveInputStream.getNextEntry)
      .takeWhile(_ != null)
      .filterNot(_.isDirectory)
      .map(entry => {
        val childArchiveType = ArchiveType.fromFileName(entry.getName)
        val iis = if (childArchiveType.isGZip) new GZIPInputStream(archiveInputStream) else archiveInputStream
        val fileName = Paths.get(basedir, entry.getName).toString

        if (childArchiveType.isArchive) {
          getFileListRecursive(fileName, childArchiveType.getArchiverName(), iis)
        } else if (childArchiveType.isGZip) {
          List(Paths.get(fileName, toUngzippedName(entry.getName)).toString)
        } else {
          List(fileName)
        }
      })
      .flatten
      .toList
  }

  def getFileList(fileName: String): Seq[String] = {

    try {
      val archiveType = ArchiveType.fromFileName(fileName)
      val is = if (archiveType.isGZip) {
        new GZIPInputStream(new FileInputStream(fileName))
      } else {
        new FileInputStream(fileName)
      }
      if (archiveType.isArchive) {
        getFileListRecursive("", archiveType.getArchiverName(), is)
      } else if (archiveType.isGZip) {
        List(Paths.get(Paths.get(fileName).getFileName.toString, toUngzippedName(fileName)).toString)
      } else {
        List(Paths.get(fileName).getFileName.toString)
      }
    } finally {
      if (is != null) {
        is.close()
      }
    }
  }

  private def getTargetStreamRecursive(basedir: String, archiveName: String, is: InputStream, targetFileName: String): Option[InputStream] = {

    val archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(archiveName, is)
    Iterator.continually(archiveInputStream.getNextEntry())
      .takeWhile(_ != null)
      .filterNot(_.isDirectory)
      .first(entry => {
        val childArchiveType = ArchiveType.fromFileName(entry.getName)
        val iis = if (childArchiveType.isGZip) {
          new GZIPInputStream(archiveInputStream)
        } else {
          archiveInputStream
        }

        val fileName = Paths.get(basedir, entry.getName).toString
        if (childArchiveType.isArchive) {
          val s = getTargetStreamRecursive(fileName, childArchiveType.getArchiverName(), iis, targetFileName)
          if (s != null) {
            return s
          }
        } else if (childArchiveType.isGZip) {
          if (targetFileName == Paths.get(fileName, toUngzippedName(entry.getName)).toString) {
            return Some(iis)
          }
        } else if (fileName == targetFileName) {
          return Some(iis)
        }
      })

    return None
  }

  def getTargetStream(archiveFileName: String, targetFileName: String): Option[InputStream] = {

    val archiveType = ArchiveType.fromFileName(archiveFileName)
    val is =
      if (archiveType.isGZip) new GZIPInputStream(new FileInputStream(archiveFileName))
      else new FileInputStream(archiveFileName)

    if (archiveType.isArchive) {
      return getTargetStreamRecursive("", archiveType.getArchiverName(), is, targetFileName)
    } else if (archiveType.isGZip) {
      if (toUngzippedName(archiveFileName) == targetFileName) {
        return Some(is)
      }
    }
    return Some(is)
  }

  private def toUngzippedName(fileName: String): String = {
    val gzName = if (fileName.contains(File.separator)) {
      fileName.substring(fileName.lastIndexOf(File.separator) + 1)
    } else {
      fileName
    }
    gzName.substring(0, gzName.lastIndexOf(".gz"))
  }
}
