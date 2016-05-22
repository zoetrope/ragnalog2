package com.arielnetworks.ragnalog.port.adapter.actor

import akka.actor.Actor
import com.arielnetworks.ragnalog.application.{RegistrationProtocol, RegistrationResult}

class WorkerActor extends Actor {
  import RegistrationProtocol._

  override def receive: Receive = {
    case msg: EmbulkInvokeRegistrationMessage =>
      println(s"WorkerActor.receive: $msg")
      Thread.sleep(10000)
      msg.sender ! new RegistrationResult("SUCCESS", "YAML PATH", Array.empty[Byte])
      println(s"WorkerActor.finished: $msg")
      context.stop(self)
  }
}
