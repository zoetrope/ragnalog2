package com.arielnetworks.ragnalog.application

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Sink, Source}

object Main {

  def main(args: Array[String]): Unit = {
    implicit val system = ActorSystem()
    implicit val materializer = ActorMaterializer()

    val source = Source[Int](1 to 5)
    val sink = Sink.foreach[Int](println)
    source.map(_ * 2).runWith(sink)
  }

}
