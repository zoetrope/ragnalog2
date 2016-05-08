package com.arielnetworks.ragnalog.domain.model.archive

import java.security.MessageDigest
import java.util.regex.Pattern

import com.arielnetworks.ragnalog.domain.model.common.{Entity, Identifier}
import com.arielnetworks.ragnalog.domain.model.container.ContainerId
import com.arielnetworks.ragnalog.domain.model.detector.{LogTypeDetectorByFilename, LogTypePattern}
import com.arielnetworks.ragnalog.domain.model.logfile.{LogFile, LogFileId, Unregistered}
import com.arielnetworks.ragnalog.support.ArchiveUtil
import org.joda.time.DateTime

import scalax.file.Path

case class ArchiveId(id: String, parent: String) extends Identifier[String, String]

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

  def extractLogFiles(containerId: ContainerId): Seq[LogFile] = {
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

  private def logFile(logName: String): LogFile = {
    val logFileId = md5(id + logName) //TODO:
    val logType = detector.detect(logName)

    val log = new LogFile(LogFileId(logFileId, id.id), id.parent, fileName, logName, logType, Unregistered, None, None, None, None, None, None, None)
    println(s"logFile: ${log}")
    log
  }

  def remove() = ???
}

