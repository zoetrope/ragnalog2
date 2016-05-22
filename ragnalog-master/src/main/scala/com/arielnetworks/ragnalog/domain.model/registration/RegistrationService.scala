package com.arielnetworks.ragnalog.domain.model.registration

import com.arielnetworks.ragnalog.domain.model.logfile.LogFile
import com.arielnetworks.ragnalog.port.adapter.service.RegistrationJob

import scala.concurrent.Future

trait RegistrationService {

  def register(logFile: LogFile): Future[Unit]

  def unregister(logFile: LogFile): Future[Unit]

  def jobs(): Future[Seq[RegistrationJob]]

  def cancel: Future[Unit]

  def status: Future[Unit]

}
