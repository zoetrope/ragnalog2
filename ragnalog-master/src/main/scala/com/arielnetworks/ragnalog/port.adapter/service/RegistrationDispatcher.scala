package com.arielnetworks.ragnalog.port.adapter.service

import akka.actor.ActorRef
import com.arielnetworks.ragnalog.domain.model.logfile.LogFile
import com.arielnetworks.ragnalog.domain.model.registration.RegistrationService
import org.joda.time.DateTime

import scala.concurrent.Future

class RegistrationDispatcher
(
  dispatcherActor: ActorRef
)
  extends RegistrationService {

  import DispatcherProtocol._

  override def register(logFile: LogFile): Future[Unit] = {

    val job = new RegistrationJob(logFile, DateTime.now(), 0)
    dispatcherActor ! job

    Future.successful(Unit)
  }

  override def unregister(logFile: LogFile): Future[Unit] = ???

  override def jobs(): Future[Seq[RegistrationJob]] = ???

  override def cancel: Future[Unit] = ???

  override def status: Future[Unit] = ???

}

