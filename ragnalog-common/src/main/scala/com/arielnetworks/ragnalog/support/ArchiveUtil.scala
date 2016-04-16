package com.arielnetworks.ragnalog.support

import java.io.{File, FileInputStream, InputStream}
import java.nio.file.Paths
import java.util.zip.GZIPInputStream

import com.arielnetworks.ragnalog.domain.model.archive.ArchiveType
import org.apache.commons.compress.archivers.ArchiveStreamFactory
import com.arielnetworks.ragnalog.support.LoanSupport._

object ArchiveUtil {

  private def getFileListRecursive(basedir: String, archiverName: String, is: InputStream): Seq[String] = {
    val archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(archiverName, is)

    Iterator.continually(archiveInputStream.getNextEntry)
      .takeWhile(_ != null)
      .filterNot(_.isDirectory)
      .map(entry => {
        val childArchiveType = ArchiveType.fromExtension(entry.getName)
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

    val archiveType = ArchiveType.fromExtension(fileName)

    using(if (archiveType.isGZip) {
      new GZIPInputStream(new FileInputStream(fileName))
    } else {
      new FileInputStream(fileName)
    }) {
      is =>
        if (archiveType.isArchive) {
          getFileListRecursive("", archiveType.getArchiverName(), is)
        } else if (archiveType.isGZip) {
          List(Paths.get(Paths.get(fileName).getFileName.toString, toUngzippedName(fileName)).toString)
        } else {
          List(Paths.get(fileName).getFileName.toString)
        }
    }
  }

  private def getTargetStreamRecursive(basedir: String, archiveName: String, is: InputStream, targetFileName: String): Option[InputStream] = {

    val archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(archiveName, is)
    Iterator.continually(archiveInputStream.getNextEntry)
      .takeWhile(_ != null)
      .filterNot(_.isDirectory)
      .map(entry => {
        val childArchiveType = ArchiveType.fromExtension(entry.getName)
        val iis = if (childArchiveType.isGZip) {
          new GZIPInputStream(archiveInputStream)
        } else {
          archiveInputStream
        }
        val fileName = Paths.get(basedir, entry.getName).toString
        if (childArchiveType.isArchive) {
          getTargetStreamRecursive(fileName, childArchiveType.getArchiverName(), iis, targetFileName)
        } else if (childArchiveType.isGZip) {
          if (targetFileName == Paths.get(fileName, toUngzippedName(entry.getName)).toString) {
            Some(iis)
          } else {
            None
          }
        } else if (fileName == targetFileName) {
          Some(iis)
        } else {
          None
        }
      }).collectFirst { case Some(i) => i }
  }

  def getTargetStream(archiveFileName: String, targetFileName: String): Option[InputStream] = {
    val archiveType = ArchiveType.fromExtension(archiveFileName)
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
    None
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
