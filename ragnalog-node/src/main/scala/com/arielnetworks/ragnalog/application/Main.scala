package com.arielnetworks.ragnalog.application

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import com.typesafe.config._
import akka.actor._
import com.arielnetworks.ragnalog.port.adapter.actor.RegistrationActor
import org.slf4j.LoggerFactory

object Main {

  def main(args: Array[String]): Unit = {

    implicit val system: ActorSystem = ActorSystem("ragnalog-node")
    //    implicit val materializer = ActorMaterializer()

    val logger = LoggerFactory.getLogger("Main")
    val config = ConfigFactory.load()
    logger.info(s"config = ${config.getConfig("ragnalog-node")}")

    system.actorOf(Props[RegistrationActor], "registration")
  }

}
