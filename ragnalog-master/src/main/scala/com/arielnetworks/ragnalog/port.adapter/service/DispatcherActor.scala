package com.arielnetworks.ragnalog.port.adapter.service

import java.io.ByteArrayOutputStream
import java.util.zip.{ZipEntry, ZipOutputStream}

import akka.actor.{Actor, ActorSelection}
import akka.pattern.ask
import akka.util.Timeout
import com.arielnetworks.ragnalog.application.RegistrationProtocol
import com.arielnetworks.ragnalog.support.ArchiveUtil

import scala.collection.mutable.ListBuffer
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Await, Future}

class DispatcherActor(registrationActors: Seq[ActorSelection]) extends Actor {

  import RegistrationProtocol._
  import DispatcherProtocol._

  val jobQueue: ListBuffer[RegistrationJob] = ListBuffer.empty

  override def receive: Receive = {
    case msg: RegistrationJob => {
      println(s"DispatcherActor.receive: $msg")
      enqueue(msg)
      dispatch()
    }
    case res: Registered => {
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

  def dispatch(): Unit = {
    println("** dispatch")

    import scala.concurrent.duration._
    implicit val timeout = Timeout(2.seconds)
    val acceptableActors = registrationActors.map(actor => {
      (actor ? Acceptable).mapTo[Boolean].map(b => (b, actor))
    })
    val firstActor = Await.result(Future.sequence(acceptableActors), 2.seconds) //don't want dispatch() to run in multi-thread
      .collect { case (true, actor: ActorSelection) => actor }.headOption
    println(s"** dispatch firstActor: $firstActor")

    firstActor.foreach(actor => {
      dequeue().foreach(firstMsg => {
        println(s"** dispatch firstMsg: $firstMsg")
        println(s"** dispatch sent")

        val bas = new ByteArrayOutputStream()
        val zos = new ZipOutputStream(bas)
        val target = ArchiveUtil.getTargetStream(firstMsg.archiveFilePath, firstMsg.logName)
        val is = target.get //TODO: errorHandling
        zos.putNextEntry(new ZipEntry("content"))
        Stream.continually(is.read).takeWhile(_ != -1).foreach(b => zos.write(b))
        zos.closeEntry()
        zos.close()

        actor ? Registration(
          firstMsg.logType,
          firstMsg.extra,
          "ragnalog-" + firstMsg.archiveName + "-" + firstMsg.logName,
          bas.toByteArray,
          this.self
        )
        //TODO: not accepted -> enqueue
      })
    })

  }
}

