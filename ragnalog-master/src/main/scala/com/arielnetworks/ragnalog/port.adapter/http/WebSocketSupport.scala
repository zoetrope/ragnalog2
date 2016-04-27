package com.arielnetworks.ragnalog.port.adapter.http

import java.util.UUID

import akka.actor.{ActorSystem, Props}
import akka.http.scaladsl.model.ws.{Message, TextMessage}
import akka.stream.scaladsl.{Flow, GraphDSL, Merge, Sink, Source}
import akka.stream.{FlowShape, OverflowStrategy}

class WebSocketSupport(system: ActorSystem) {

  val broadcastActor = system.actorOf(Props[BroadcastActor])

  def webSocketFlow = Flow.fromGraph(GraphDSL.create(Source.actorRef[WSMessage](bufferSize = 3, OverflowStrategy.fail)) {
    implicit builder =>
      subscribeSource =>

        println(s"source: $subscribeSource")
        import GraphDSL.Implicits._

        val fromWebSocketFlow = builder.add(Flow[Message].collect {
          case TextMessage.Strict(text) => WSMessage(text)
        })

        val merge = builder.add(Merge[WebSocketEvent](2))
        val uuid = UUID.randomUUID().toString
        val acceptanceFlow = builder.materializedValue.map[WebSocketEvent](newActor => Open(uuid, newActor))
        val broadcastActorSink = Sink.actorRef(broadcastActor, Close(uuid))

        val toWebSocketFlow = builder.add(
          Flow[WSMessage].map {
            case t: WSMessage =>
              TextMessage(t.title)
          }
        )

        fromWebSocketFlow ~> Flow[WebSocketEvent].map(x => {
          println(s"!!websocket: $x")
          x
        }) ~> merge ~> broadcastActorSink
        acceptanceFlow ~> Flow[WebSocketEvent].map(x => {
          println(s"!!acceptance: $x")
          x
        }) ~> merge
        subscribeSource ~> Flow[WSMessage].map(x => {
          println(s"!!subscribe: $x")
          x
        }) ~> toWebSocketFlow
        FlowShape(fromWebSocketFlow.in, toWebSocketFlow.out)
  })
}
