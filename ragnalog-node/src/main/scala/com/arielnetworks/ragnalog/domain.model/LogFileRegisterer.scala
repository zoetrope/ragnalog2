package com.arielnetworks.ragnalog.domain.model

import com.arielnetworks.ragnalog.application.RegistrationProtocol
import com.arielnetworks.ragnalog.port.adapter.embulk.EmbulkRegistrationResult

import scala.concurrent.Future

trait LogFileRegisterer {
  import RegistrationProtocol._ //TODO: remove

  def register(command: EmbulkInvokeRegistrationMessage): Future[EmbulkRegistrationResult]
}
