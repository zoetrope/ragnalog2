package com.arielnetworks.ragnalog.port.adapter.service

import akka.actor.{Actor, ActorRef, ActorSelection, Props}


object DispatcherActor {

  def props(registrationActors: Seq[ActorSelection]) =
    Props(new DispatcherActor(registrationActors))

  case class RegistrationJob()

  case class StatusJob()

}

class DispatcherActor(registrationActors: Seq[ActorSelection]) extends Actor {

  import DispatcherActor._

  override def receive: Receive = {
    case RegistrationJob => sender ! dispatch()
    case StatusJob =>
  }

  def dispatch(): String = {

    registrationActors.foreach(actor => {
    })

    "reply"
  }
}
