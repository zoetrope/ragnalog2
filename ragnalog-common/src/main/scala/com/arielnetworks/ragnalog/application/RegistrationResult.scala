package com.arielnetworks.ragnalog.application

import scalax.file.Path

sealed trait ResultType

case class RegistrationResult
(
  resultType: String,
  yaml: String,
  zippedLog: Array[Byte]
)
