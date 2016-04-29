package com.arielnetworks.ragnalog.port.adapter.http.notification

import akka.actor.{Actor, ActorRef}

import scala.collection.mutable

class BroadcastActor extends Actor {

  val subscribers = mutable.HashMap.empty[String, ActorRef]

  def receive: Receive = {
    case Open(id, actorRef) => subscribers += ((id, actorRef))
    case Close(id) => subscribers -= id
    case msg => subscribers.values.foreach(_ ! msg)
  }

}
