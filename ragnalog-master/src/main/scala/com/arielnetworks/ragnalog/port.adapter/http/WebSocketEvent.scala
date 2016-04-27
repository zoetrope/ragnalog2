package com.arielnetworks.ragnalog.port.adapter.http

import akka.actor.ActorRef

sealed trait WebSocketEvent

case class Open(id: String, actorRef: ActorRef) extends WebSocketEvent

case class Close(id: String) extends WebSocketEvent

case class WSMessage(title: String) extends WebSocketEvent
