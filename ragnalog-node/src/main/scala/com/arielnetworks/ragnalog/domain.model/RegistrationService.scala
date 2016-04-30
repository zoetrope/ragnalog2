package com.arielnetworks.ragnalog.domain.model

import com.arielnetworks.ragnalog.application.{RegistrationCommand, RegistrationResult}

import scala.concurrent.Future

trait RegistrationService {

  def register(command: RegistrationCommand): Future[RegistrationResult]
}
