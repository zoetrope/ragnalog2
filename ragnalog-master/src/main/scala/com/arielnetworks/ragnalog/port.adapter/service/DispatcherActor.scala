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
      dispatch()
    }
    case x => {
      println(s"DispatcherActor.receive unknown: $x")
    }
  }

  def enqueue(msg: InvokeRegistrationMessage) = {
    msgQueue += msg
  }

  def dequeue(): Option[InvokeRegistrationMessage] = {
    val msg = msgQueue.sortBy(_.priority).headOption
    msg.map(m => {
      msgQueue -= m
      m.sender = this.self
      m
    })
  }

  def dispatch() = {

    println("** dispatch")

    dequeue().foreach(firstMsg => {
      println(s"** dispatch firstMsg: $firstMsg")
      import scala.concurrent.duration._
      implicit val timeout = Timeout(2.seconds)
      val acceptableActors = registrationActors.map(actor => {
        (actor ? "ok").mapTo[Boolean].map(b => (b, actor))
      })

      for {
        actors <- Future.sequence(acceptableActors)
        _ = println(actors)
        actorOpt = actors.collect { case (true, a: ActorSelection) => a }.headOption
        _ <- actorOpt match {
          case Some(actor) => {
            println(s"** dispatch sent")
            actor ? firstMsg
            //TODO: not accepted -> enqueue
          }
          case None =>
            println(s"** dispatch enqueue")
            enqueue(firstMsg)
            Future.successful(())
        }

      } yield ()
    })
  }
}

