package com.arielnetworks.ragnalog.domain.model.logfile

import com.arielnetworks.ragnalog.domain.model.common.{Entity, Identifier}
import org.joda.time.DateTime

sealed trait LogStatus {
  def of(value: String): LogStatus = value match {
    case "Uploading" => Uploading
    case "Uploaded" => Uploaded
    case "Registering" => Registering
    case "Registered" => Registered
    case "Unregistered" => Unregistered
    case "Error" => Error
  }
}

object LogStatus extends LogStatus

case object Uploading extends LogStatus

case object Uploaded extends LogStatus

case object Registering extends LogStatus

case object Registered extends LogStatus

case object Unregistered extends LogStatus

case object Error extends LogStatus

case class LogFileId(value: String) extends Identifier[String]

case class LogFile
(
  id: LogFileId,
  filePath: String,
  fileType: Option[String],
  status: LogStatus,
  index: Option[String],
  from: Option[DateTime],
  to: Option[DateTime],
  extra: Option[String],
  count: Option[Long],
  errorCount: Option[Long],
  errorMessage: Option[String]
) extends Entity[LogFileId] {

  def update(fileType: String, extra: Option[String], status: LogStatus) = ???

}
