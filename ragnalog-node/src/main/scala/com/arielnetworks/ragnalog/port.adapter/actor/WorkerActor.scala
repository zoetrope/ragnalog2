package com.arielnetworks.ragnalog.port.adapter.actor

import akka.actor.Actor
import akka.actor.Actor.Receive
import com.arielnetworks.ragnalog.application.{InvokeRegistrationMessage, RegistrationResult}

import scalax.file.Path

class WorkerActor extends Actor {
  override def receive: Receive = {
    case msg: InvokeRegistrationMessage =>
      println(s"WorkerActor.receive: $msg")
      Thread.sleep(10000)
      msg.sender ! new RegistrationResult("SUCCESS", "YAML PATH", Array.empty[Byte])
      println(s"WorkerActor.finished: $msg")
      context.stop(self)
  }
}
