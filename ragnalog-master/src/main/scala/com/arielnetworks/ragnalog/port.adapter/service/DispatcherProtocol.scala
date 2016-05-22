package com.arielnetworks.ragnalog.port.adapter.service

import akka.actor.ActorRef
import com.arielnetworks.ragnalog.domain.model.logfile.LogFile
import org.joda.time.DateTime

import scalax.file.Path

object DispatcherProtocol {

  case class RegistrationJob
  (
    archiveFilePath: Path,
    archiveName: String,
    logName: String,
    logType: String,
    extra: Option[String],
    invokedTime: DateTime,
    priority: Int
  )

}
