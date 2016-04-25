package com.arielnetworks.ragnalog.application.http

import akka.actor.Props
import akka.stream.actor.ActorPublisher

import scala.concurrent.ExecutionContext


class MyPublisher extends ActorPublisher[MyData]{

  override def preStart = {
    context.system.eventStream.subscribe(self, classOf[MyData])
  }

  override def receive: Receive = {

    case msg: MyData ⇒
      if (isActive && totalDemand > 0) {
        onNext(msg)
      }
  }
}

object MyPublisher {
  def props(implicit ctx: ExecutionContext): Props = Props(new MyPublisher())
}

case class MyData(data:String)