package com.arielnetworks.ragnalog.port.adapter.service

import akka.actor.{Actor, ActorSelection}
import akka.pattern.ask
import akka.util.Timeout
import com.arielnetworks.ragnalog.application.{RegistrationProtocol, RegistrationResult}

import scala.collection.mutable.ListBuffer
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class DispatcherActor(registrationActors: Seq[ActorSelection]) extends Actor {
  import RegistrationProtocol._

  val jobQueue: ListBuffer[RegistrationJob] = ListBuffer.empty

  override def receive: Receive = {
    case msg: RegistrationJob => {
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

  def enqueue(msg: RegistrationJob) = {
    jobQueue += msg
  }

  def dequeue(): Option[RegistrationJob] = {
    val msg = jobQueue.sortBy(_.priority).headOption
    msg.map(m => {
      jobQueue -= m
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
            actor ? EmbulkInvokeRegistrationMessage(
              firstMsg.logFile.logType.toString,
              firstMsg.logFile.extra.getOrElse(""),
              "ragnalog-" + firstMsg.logFile.archiveName + firstMsg.logFile.logName,
              null,
              this.self
            )
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

