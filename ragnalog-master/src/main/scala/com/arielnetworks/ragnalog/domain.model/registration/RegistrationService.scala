package com.arielnetworks.ragnalog.domain.model.registration

import com.arielnetworks.ragnalog.domain.model.archive.Archive
import com.arielnetworks.ragnalog.domain.model.logfile.LogFile
import com.arielnetworks.ragnalog.port.adapter.service.DispatcherProtocol

import scala.concurrent.Future

trait RegistrationService {
  import DispatcherProtocol._ //TODO: remove

  def register(logFile: LogFile, archive: Archive): Future[Unit]

  def unregister(logFile: LogFile): Future[Unit]

  def jobs(): Future[Seq[RegistrationJob]]

  def cancel: Future[Unit]

  def status: Future[Unit]

}
