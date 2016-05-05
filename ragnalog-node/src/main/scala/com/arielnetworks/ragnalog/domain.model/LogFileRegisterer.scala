package com.arielnetworks.ragnalog.domain.model

import com.arielnetworks.ragnalog.application.{InvokeRegistrationMessage, RegistrationResult}

import scala.concurrent.Future

trait LogFileRegisterer {

  def register(command: InvokeRegistrationMessage): Future[RegistrationResult]
}
