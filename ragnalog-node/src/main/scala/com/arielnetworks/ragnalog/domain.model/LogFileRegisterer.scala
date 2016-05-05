package com.arielnetworks.ragnalog.domain.model

import com.arielnetworks.ragnalog.port.adapter.embulk.{EmbulkInvokeRegistrationMessage, EmbulkRegistrationResult}

import scala.concurrent.Future

trait LogFileRegisterer {

  def register(command: EmbulkInvokeRegistrationMessage): Future[EmbulkRegistrationResult]
}
