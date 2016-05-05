package com.arielnetworks.ragnalog.port.adapter.actor

import akka.actor.Actor
import akka.actor.Actor.Receive
import com.arielnetworks.ragnalog.application.{CommandSuccess, InvokeRegistrationMessage, RegistrationResult}

import scalax.file.Path

class WorkerActor extends Actor {
  override def receive: Receive = {
    case msg: InvokeRegistrationMessage =>
      println(s"WorkerActor.receive: $msg")
      Thread.sleep(10000)
      sender ! new RegistrationResult(CommandSuccess(), Path(""), Array.empty[Byte])
      context.stop(self)
  }
}
