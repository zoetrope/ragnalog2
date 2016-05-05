package com.arielnetworks.ragnalog.domain.model.registration

import com.arielnetworks.ragnalog.domain.model.container.Container
import com.arielnetworks.ragnalog.domain.model.logfile.LogFile

import scala.concurrent.Future

trait RegistrationAdapter {

  def register(logFile: LogFile): Future[Unit] = ???

  def unregister(logFile: LogFile): Future[Unit] = ???

  def remove(container: Container): Future[Unit] = ???

}
