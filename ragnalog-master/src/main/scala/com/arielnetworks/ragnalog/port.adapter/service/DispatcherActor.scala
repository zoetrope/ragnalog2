package com.arielnetworks.ragnalog.port.adapter.service

import akka.actor.Actor
import akka.actor.Actor.Receive

case class RegistrationJob()
case class StatusJob()

class DispatcherActor extends Actor{

  override def receive: Receive = {
    case RegistrationJob => sender ! "reply"
    case StatusJob =>
  }
}
