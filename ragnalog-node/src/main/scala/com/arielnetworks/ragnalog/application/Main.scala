package com.arielnetworks.ragnalog.application

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import com.typesafe.config._
import akka.actor._
import com.arielnetworks.ragnalog.port.adapter.actor.RegistrationActor

object Main {

  def main(args: Array[String]): Unit = {

    implicit val system: ActorSystem = ActorSystem("ragnalog-node")
    //    implicit val materializer = ActorMaterializer()

    println(system.settings)

    system.actorOf(Props[RegistrationActor], "registration")
  }

}
