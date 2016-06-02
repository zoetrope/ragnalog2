package com.arielnetworks.ragnalog.port.adapter.actor

import akka.actor.{Actor, ActorSystem, Props}
import org.scalatest._
import akka.testkit.{TestActorRef, TestKit, TestProbe}

import scala.concurrent.duration._
import scala.concurrent.Await
import akka.pattern.ask
import akka.util.Timeout
import com.arielnetworks.ragnalog.application.RegistrationProtocol
import org.joda.time.DateTime

import scala.collection.mutable.ListBuffer
import scala.util.Success
import scalax.file.Path

class MockedWorker extends Actor {
  def receive = {
    case 'ping => sender ! 'pong
  }
}

class BrokerSpec
  extends TestKit(ActorSystem("RagnalogSpec"))
    with FunSpecLike with DiagrammedAssertions
    with BeforeAndAfterAll {

  import RegistrationProtocol._

  override def afterAll(): Unit = {
    system.terminate()
  }

  val brokerActor = system.actorOf(Props(classOf[RegistrationBroker], Props[MockedWorker], 3))

  describe("Registration"){

  }
}

