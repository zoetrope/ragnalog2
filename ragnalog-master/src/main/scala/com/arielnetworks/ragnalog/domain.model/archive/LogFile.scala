package com.arielnetworks.ragnalog.domain.model.archive

import com.arielnetworks.ragnalog.domain.model.common.{Entity, Identifier}
import org.joda.time.DateTime

sealed trait LogStatus {
  def of(value: String): LogStatus = {
    case "Uploading" => Uploading
    case "Uploaded" => Uploaded
    case "Registering" => Registering
    case "Registered" => Registered
    case "Unregistered" => Unregistered
    case "Error" => Error
    case _ => None
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
) extends Entity[LogFileId]
