package com.arielnetworks.ragnalog.port.adapter.actor

import akka.actor.{Actor, Props}
import akka.pattern.ask
import akka.util.Timeout
import com.arielnetworks.ragnalog.application._

class RegistrationBroker(workerProps: Props, workerLimit: Int) extends Actor {
  import RegistrationProtocol._

  def receive: Receive = {
    case command: Registration => {
      println(s"BrokerActor.receive: $command")

      import scala.concurrent.duration._
      implicit val timeout = Timeout(100.minutes)

      if (context.children.size < workerLimit) {
        println("accepted")
        val worker = context.actorOf(workerProps)
        context.watch(worker)
        sender ! Accepted
        worker ! command
      } else {
        println("not accepted")
        sender ! NotAccepted
      }
    }
    case  Acceptable => {
      println(s"BrokerActor.receive: ${context.children.size < workerLimit}")
      sender ! (context.children.size < workerLimit)
    }
    case x => {
      println(s"Broker: unknown message: $x")
    }

    //    case Terminated(x) => { }
  }
}

