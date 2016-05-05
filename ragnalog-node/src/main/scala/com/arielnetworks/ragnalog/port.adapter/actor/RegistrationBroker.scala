package com.arielnetworks.ragnalog.port.adapter.actor

import akka.actor.{Actor, Props}
import akka.pattern.ask
import akka.util.Timeout
import com.arielnetworks.ragnalog.application.{AcceptedMessage, InvokeRegistrationMessage, NotAcceptedMessage, RegistrationResult}

import scala.concurrent.ExecutionContext.Implicits.global

class RegistrationBroker extends Actor {

  val workerLimit = 3

  def receive: Receive = {
    case command: InvokeRegistrationMessage => {
      println(s"BrokerActor.receive: $command")

      import scala.concurrent.duration._
      implicit val timeout = Timeout(100.minutes)

      if (context.children.size < workerLimit) {
        println("accepted")
        val worker = context.actorOf(Props[WorkerActor])
        context.watch(worker)
        sender ! AcceptedMessage
        worker ! command
      } else {
        println("not accepted")
        sender ! NotAcceptedMessage
      }
    }
    case msg: String => {
      println(s"BrokerActor.receive: ${context.children.size < workerLimit}")
      sender ! (context.children.size < workerLimit)
    }
    case x => {
      println(s"Broker: unknown message: $x")
    }

    //    case Terminated(x) => { }
  }
}

