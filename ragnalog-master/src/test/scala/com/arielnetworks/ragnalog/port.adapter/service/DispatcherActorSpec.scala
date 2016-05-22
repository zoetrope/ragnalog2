package com.arielnetworks.ragnalog.port.adapter.service

import akka.actor.{ActorSystem, Props}
import org.scalatest._
import akka.testkit.{TestActorRef, TestKit, TestProbe}

import scala.concurrent.duration._
import scala.concurrent.Await
import akka.pattern.ask
import com.arielnetworks.ragnalog.application.RegistrationProtocol.EmbulkInvokeRegistrationMessage
import com.arielnetworks.ragnalog.domain.model.logfile.{LogFile, LogFileId, LogStatus, Registering}
import org.joda.time.DateTime

class DispatcherActorSpec
  extends TestKit(ActorSystem("RagnalogSpec"))
    with FunSpecLike with DiagrammedAssertions
    with BeforeAndAfterAll {

  override def afterAll(): Unit = {
    system.terminate()
  }

  //test pattern
  // * job queue is full
  // * dispatch to multiple broker
  // * priority
  // * cancel
  // * run the next job

  describe("Registration") {
    it("should accept message") {
      val brokerProbe = new TestProbe(system)
      val selection = system.actorSelection(brokerProbe.ref.path)
      val dispatcherActor = system.actorOf(Props(classOf[DispatcherActor], Seq(selection)))

      val log = LogFile(
        LogFileId("logFileId", "archiveId"),
        "containerId",
        "archiveName",
        "logName",
        Some("logType"),
        Registering,
        None,
        None,
        None,
        Some("extra"),
        None,
        None,
        None
      )
      val job = RegistrationJob(log,DateTime.now,0)
      dispatcherActor ! job

      brokerProbe.expectMsg("ok")
      brokerProbe.reply(true)
      brokerProbe.expectMsg(EmbulkInvokeRegistrationMessage("logType", Some("extra"), "ragnalog-archiveName-logName", null, dispatcherActor))

    }
  }

}
