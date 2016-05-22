package com.arielnetworks.ragnalog.port.adapter.service

import akka.actor.ActorRef
import com.arielnetworks.ragnalog.domain.model.logfile.LogFile
import org.joda.time.DateTime

case class RegistrationJob
(
  logFile: LogFile,
  invokedTime: DateTime,
  priority: Int
)

