package com.arielnetworks.ragnalog.application

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import com.typesafe.config._
import akka.actor._
import com.arielnetworks.ragnalog.port.adapter.actor.RegistrationActor

object Main {

  def main(args: Array[String]): Unit = {
    val conf =
      """
        |akka {
        |  actor {
        |    provider = "akka.remote.RemoteActorRefProvider"
        |  }
        |  remote {
        |    enabled-transports = ["akka.remote.netty.tcp"]
        |    netty.tcp {
        |      hostname = "0.0.0.0"
        |      port = 2551
        |    }
        |  }
        |}
      """.stripMargin

    val config = ConfigFactory.parseString(conf)

    implicit val system: ActorSystem = ActorSystem("ragnalog-node", config)
//    implicit val materializer = ActorMaterializer()

    system.actorOf(Props[RegistrationActor], "registration")
  }

}
