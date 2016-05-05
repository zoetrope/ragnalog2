package com.arielnetworks.ragnalog.port.adapter.service

import akka.actor.{Actor, ActorRef, ActorSelection, Props}
import com.arielnetworks.ragnalog.application.{InvokeRegistrationMessage, RegistrationResult}

import scala.collection.mutable.ListBuffer
import akka.pattern.ask
import akka.util.Timeout

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

class DispatcherActor(registrationActors: Seq[ActorSelection]) extends Actor {

  val msgQueue: ListBuffer[InvokeRegistrationMessage] = ListBuffer.empty

  override def receive: Receive = {
    case msg: InvokeRegistrationMessage => {
      println(s"DispatcherActor.receive: $msg")
      enqueue(msg)
      dispatch()
    }
    case res: RegistrationResult => {
      println(s"DispatcherActor.receive: $res")
    }
    case x => {
      println(s"DispatcherActor.receive unknown: $x")
    }
  }

  def enqueue(msg: InvokeRegistrationMessage) = {
    msgQueue += msg
  }

  def dequeue(): InvokeRegistrationMessage = {
    val msg = msgQueue.sortBy(_.priority).head
    msg.sender = this.self
    msg
  }

  def dispatch(): Future[Unit] = {
    val firstMsg = dequeue()

    import scala.concurrent.duration._
    implicit val timeout = Timeout(20.seconds)
    val acceptableActors = registrationActors.map(actor => {
      (actor ? "ok").mapTo[Boolean].map(b => (b, actor))
    })

    for {
      actors <- Future.sequence(acceptableActors)
      _ = println(s"actors: $actors")
      actorOpt = actors.collect { case (true, a: ActorSelection) => a }.headOption
      actor = actorOpt.get //TODO:
      res <- actor ? firstMsg
      _ = println(s"sent: $res")
    //      res = actorOpt.map(actor => actor ? firstMsg).foreach(res => println("sent: $res"))
    } yield ()

  }
}

