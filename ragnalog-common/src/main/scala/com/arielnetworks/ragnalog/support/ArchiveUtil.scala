package com.arielnetworks.ragnalog.support

import java.io.{File, FileInputStream, InputStream}
import java.net.URLDecoder

import scalax.file.Path
import java.util.zip.GZIPInputStream

import com.arielnetworks.ragnalog.domain.model.archive.ArchiveType
import org.apache.commons.compress.archivers.ArchiveStreamFactory
import com.arielnetworks.ragnalog.support.LoanSupport._

object ArchiveUtil {

  private def getFileListRecursive(basedir: Path, archiverName: String, is: InputStream): Seq[String] = {
    val factory = new ArchiveStreamFactory()
    //    factory.setEntryEncoding("SJIS")
    val archiveInputStream = factory.createArchiveInputStream(archiverName, is)

    Iterator.continually(archiveInputStream.getNextEntry)
      .takeWhile(_ != null)
      .filterNot(_.isDirectory)
      .map(entry => {
        val childArchiveType = ArchiveType.fromExtension(entry.getName)
        val iis = if (childArchiveType.isGZip) new GZIPInputStream(archiveInputStream) else archiveInputStream
        val fileName = if (basedir.path.isEmpty) Path(entry.getName, '/') else basedir / Path(entry.getName, '/')

        if (childArchiveType.isArchive) {
          getFileListRecursive(fileName, childArchiveType.getArchiverName(), iis)
        } else if (childArchiveType.isGZip) {
          List((fileName / Path(toUngzippedName(entry.getName), '/')).path)
        } else {
          List(fileName.path)
        }
      })
      .flatten
      .toList
  }

  def getFileList(fileName: Path): Seq[String] = {

    val normalizedPath = Path(URLDecoder.decode(fileName.path, "UTF-8"), '/')

    val archiveType = ArchiveType.fromExtension(normalizedPath.path)

    using(if (archiveType.isGZip) {
      new GZIPInputStream(new FileInputStream(normalizedPath.path))
    } else {
      new FileInputStream(normalizedPath.path)
    }) {
      is =>
        if (archiveType.isArchive) {
          getFileListRecursive(Path(""), archiveType.getArchiverName(), is)
        } else if (archiveType.isGZip) {
          List(Path(normalizedPath.name, Path(toUngzippedName(normalizedPath.path), '/').path).path)
        } else {
          List(normalizedPath.name)
        }
    }
  }

  private def getTargetStreamRecursive(basedir: Path, archiveName: String, is: InputStream, targetFileName: String): Option[InputStream] = {
    val factory = new ArchiveStreamFactory()
    val archiveInputStream = factory.createArchiveInputStream(archiveName, is)
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
        val fileName = if (basedir.path.isEmpty) Path(entry.getName, '/') else basedir / Path(entry.getName, '/')
        if (childArchiveType.isArchive) {
          getTargetStreamRecursive(fileName, childArchiveType.getArchiverName(), iis, targetFileName)
        } else if (childArchiveType.isGZip) {
          if (targetFileName == (fileName / Path(toUngzippedName(entry.getName), '/')).path) {
            Some(iis)
          } else {
            None
          }
        } else if (fileName.path == targetFileName) {
          Some(iis)
        } else {
          None
        }
      }).collectFirst { case Some(i) => i }
  }

  def getTargetStream(archiveFileName: Path,targetFileName : String): Option[InputStream] = {
    val normalizedPath = Path(URLDecoder.decode(archiveFileName.path, "UTF-8"), '/')

    val archiveType = ArchiveType.fromExtension(normalizedPath.path)
    val is =
      if (archiveType.isGZip) new GZIPInputStream(new FileInputStream(normalizedPath.path))
      else new FileInputStream(normalizedPath.path)

    if (archiveType.isArchive) {
      return getTargetStreamRecursive(Path(""), archiveType.getArchiverName(), is, targetFileName)
    } else if (archiveType.isGZip) {
      if (toUngzippedName(normalizedPath.name) == targetFileName) {
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
