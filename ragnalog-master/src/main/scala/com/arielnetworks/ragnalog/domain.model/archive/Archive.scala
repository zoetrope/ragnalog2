package com.arielnetworks.ragnalog.domain.model.archive

import java.security.MessageDigest
import java.util.regex.Pattern

import com.arielnetworks.ragnalog.domain.model.common.{Entity, Identifier}
import com.arielnetworks.ragnalog.domain.model.detector.{LogTypeDetectorByFilename, LogTypePattern}
import com.arielnetworks.ragnalog.domain.model.logfile.{LogFile, LogFileId, LogStatus, Uploaded}
import com.arielnetworks.ragnalog.support.ArchiveUtil
import org.joda.time.DateTime

import scala.concurrent.Future
import scalax.file.Path

case class ArchiveId(value: String) extends Identifier[String]

case class Archive
(
  id: ArchiveId,
  fileName: String,
  filePath: Path,
  size: Long,
  uploadedDate: DateTime,
  modifiedDate: DateTime,
  fileNameEncoding: String
) extends Entity[ArchiveId] {

  def extractLogFiles(): Seq[LogFile] = {
    ArchiveUtil.getFileList(filePath).map(logFile)
  }

  //TODO:
  private val detector = new LogTypeDetectorByFilename(
    List(
      new LogTypePattern(30, Pattern.compile(".*access.*", Pattern.CASE_INSENSITIVE), "apache.access")
    )
  )

  private def md5(text: String): String = {
    MessageDigest.getInstance("MD5").digest(text.getBytes).map("%02x".format(_)).mkString
  }

  private def logFile(fileName: String): LogFile = {
    val logFileId = md5(id + fileName) //TODO:
    val logType = detector.detect(fileName)

    new LogFile(LogFileId(logFileId), fileName, logType, Uploaded, None, None, None, None, None, None, None)
  }

  def remove() = ???
}

