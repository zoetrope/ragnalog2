package com.arielnetworks.ragnalog.port.adapter.actor

import akka.actor.Actor

case class RegistrationMessage
(

)

case class GuessMessage
(

)

class RegistrationActor extends Actor {

  def receive: Receive = {
    case m => println(s"receive: $m")
    //    case msg: RegistrationMessage => {
    //
    //    }
  }
}
