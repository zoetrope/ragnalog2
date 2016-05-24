package com.arielnetworks.ragnalog.port.adapter.service

import akka.actor.{ActorSystem, Props}
import org.scalatest._
import akka.testkit.{TestActorRef, TestKit, TestProbe}

import scala.concurrent.duration._
import scala.concurrent.Await
import akka.pattern.ask
import com.arielnetworks.ragnalog.application.RegistrationProtocol
import com.arielnetworks.ragnalog.domain.model.logfile.{LogFile, LogFileId, LogStatus, Registering}
import org.joda.time.DateTime

import scalax.file.Path

class DispatcherActorSpec
  extends TestKit(ActorSystem("RagnalogSpec"))
    with FunSpecLike with DiagrammedAssertions
    with BeforeAndAfterAll {

  import RegistrationProtocol._
  import DispatcherProtocol._

  override def afterAll(): Unit = {
    system.terminate()
  }

  //test pattern
  // * job queue is full
  // * dispatch to multiple broker
  // * priority
  // * cancel
  // * run the next job
  // * don't have logType

  describe("Registration") {
    it("should accept message") {
      val brokerProbe = new TestProbe(system)
      val selection = system.actorSelection(brokerProbe.ref.path)
      val dispatcherActor = system.actorOf(Props(classOf[DispatcherActor], Seq(selection)))

      val job = RegistrationJob(
        Path(""),
        "archiveName",
        "logName",
        "logType",
        Some("extra"),
        DateTime.now,
        0
      )
      dispatcherActor ! job

      brokerProbe.expectMsg(Acceptable)
      brokerProbe.reply(true)
      brokerProbe.expectMsg(Registration("logType", Some("extra"), "ragnalog-archiveName-logName", null, dispatcherActor))

    }
  }

}
