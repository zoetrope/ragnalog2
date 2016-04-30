package com.arielnetworks.ragnalog.application

import scalax.file.Path

sealed trait ResultType

case class CommandSuccess() extends ResultType

case class CommandFailure() extends ResultType

case class RegistrationResult
(
  resultType: ResultType,
  yaml: Path,
  zippedLog: Array[Byte]
)
