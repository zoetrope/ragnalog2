package com.arielnetworks.ragnalog.port.adapter.actor

import akka.actor.{Actor, Props, Terminated}
import com.arielnetworks.ragnalog.application.{AcceptedMessage, InvokeRegistrationMessage, NotAcceptedMessage}

class RegistrationBroker extends Actor {

  val workerLimit = 1

  def receive: Receive = {
    case command: InvokeRegistrationMessage => {
      println(s"BrokerActor.receive: $command")

      if (context.children.size < workerLimit) {
        val worker = context.actorOf(Props[WorkerActor])
        context.watch(worker)
        worker ! command
        sender ! AcceptedMessage
      } else {
        sender ! NotAcceptedMessage
      }
    }
    case msg: String => {
      println("BrokerActor.receive: ok")
      sender ! true
    }

    //    case Terminated(x) => { }
  }
}

