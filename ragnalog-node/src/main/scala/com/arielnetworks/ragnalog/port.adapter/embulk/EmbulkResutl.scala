package com.arielnetworks.ragnalog.port.adapter.embulk

import scalax.file.Path

sealed trait EmbulkResultType

case class CommandSuccess() extends EmbulkResultType

case class CommandFailure() extends EmbulkResultType

case class EmbulkRegistrationResult
(
  resultType: EmbulkResultType,
  yaml: Path,
  zippedLog: Array[Byte]
)

