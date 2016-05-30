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
  // * target file not found

  val brokerProbe = new TestProbe(system)
  val selection = system.actorSelection(brokerProbe.ref.path)
  val dispatcherActor = system.actorOf(Props(classOf[DispatcherActor], Seq(selection)))

  val apacheZipPath = Path(getClass.getClassLoader.getResource("apache10.zip").getPath, '/')

  val job = RegistrationJob(
    archiveFilePath = apacheZipPath,
    archiveName = "archiveName",
    logName = "apache-access.1.log",
    logType = "logType",
    extra = Some("extra"),
    invokedTime = DateTime.now,
    priority = 0
  )

  describe("Registration") {
    it("should accept message") {
      dispatcherActor ! job

      brokerProbe.expectMsg(Acceptable)
      brokerProbe.reply(true)
      brokerProbe.expectMsgPF() {
        case Registration("logType", Some("extra"), "ragnalog-archiveName-apache-access.1.log", _, _) => ()
      }
      brokerProbe.expectNoMsg()
    }
    it("should accept high priority message") {
      val lowJob1 = job.copy(priority = 10, logName = "apache-access.10.log")
      val highJob = job.copy(priority = 5, logName = "apache-access.5.log")
      val lowJob2 = job.copy(priority = 9, logName = "apache-access.9.log")

      dispatcherActor ! lowJob1
      brokerProbe.expectMsg(Acceptable)
      brokerProbe.reply(false)

      dispatcherActor ! highJob
      brokerProbe.expectMsg(Acceptable)
      brokerProbe.reply(false)

      dispatcherActor ! lowJob2
      brokerProbe.expectMsg(Acceptable)
      brokerProbe.reply(true)

      brokerProbe.expectMsgPF() {
        case Registration("logType", Some("extra"), "ragnalog-archiveName-apache-access.5.log", _, _) => ()
      }
      brokerProbe.expectNoMsg()
    }
  }
  describe("Monitoring") {
    val jobs = dispatcherActor ? MonitoringJob
    assert(jobs === Seq.empty)
  }
}

