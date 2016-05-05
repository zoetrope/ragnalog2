package com.arielnetworks.ragnalog.port.adapter.service

import akka.actor.Props
import com.arielnetworks.ragnalog.application.{InvokeRegistrationMessage, ServiceRegistry}
import com.arielnetworks.ragnalog.domain.model.registration.RegistrationService
import com.typesafe.config.ConfigFactory

import scala.collection.JavaConverters._
import scala.concurrent.Future
import akka.pattern.ask
import akka.util.Timeout
import com.arielnetworks.ragnalog.domain.model.logfile.LogFile

class RegistrationDispatcher
(
)
  extends RegistrationService {

  //TODO:
  val system = ServiceRegistry.actorSystem
  val config = ConfigFactory.load().getConfig("ragnalog-master")
  val registrationActorPath = config.getStringList("registration.path")
  val registrationRef = registrationActorPath.asScala.map(p => system.actorSelection(p))
  val dispatcherActor = system.actorOf(Props(classOf[DispatcherActor], registrationRef))

  override def register(logFile: LogFile)(f: Any => Future[Unit]) = ???

  override def unregister(logFile: LogFile): Future[Unit] = ???

  override def cancel: Future[Unit] = ???

  override def status: Future[Unit] = ???

  def invoke(command: InvokeRegistrationMessage) = {
    println(s"RegistrationDispatcher.invoke: $command")
    import scala.concurrent.duration._
    implicit val timeout = Timeout(100.millisecond)
    val reply: Future[String] = (dispatcherActor ? command).mapTo[String]
  }

}

