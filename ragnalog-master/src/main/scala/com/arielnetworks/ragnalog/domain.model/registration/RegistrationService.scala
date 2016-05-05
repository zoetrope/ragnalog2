package com.arielnetworks.ragnalog.domain.model.registration

import com.arielnetworks.ragnalog.domain.model.logfile.LogFile

import scala.concurrent.Future

trait RegistrationService {

  def register(logFile: LogFile)(f: Any => Future[Unit]) = ???

  def unregister(logFile: LogFile): Future[Unit] = ???

  def cancel: Future[Unit] = ???

  def status: Future[Unit] = ???

}
