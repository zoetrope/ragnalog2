package com.arielnetworks.ragnalog.application

import akka.actor.ActorRef

object RegistrationProtocol {

  case class Registration
  (
    logType: String,
    extra: Option[String],
    indexName: String,
    zippedContent: Array[Byte],
    sender: ActorRef
  )

  case class Accepted()

  case class NotAccepted()

  sealed trait ResultType

  case class Registered
  (
    resultType: String,
    yaml: String,
    zippedLog: Array[Byte]
  )

  case class Acceptable()
}
