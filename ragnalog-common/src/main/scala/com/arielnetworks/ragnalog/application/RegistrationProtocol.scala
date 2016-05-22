package com.arielnetworks.ragnalog.application

import akka.actor.ActorRef

object RegistrationProtocol {

  case class EmbulkInvokeRegistrationMessage
  (
    logType: String,
    extra: String,
    indexName: String,
    zippedContent: Array[Byte],
    sender: ActorRef
  )

  case class AcceptedMessage()

  case class NotAcceptedMessage()

}
