package com.arielnetworks.ragnalog.port.adapter.embulk

import java.nio.file.Path

case class RegistrationConfiguration
(
  name: String,
  description: Option[String],
  parser: String,
  filters: Seq[String],
  timeField: String,
  template: Path,
  preprocessor: Option[String],
  doGuess: Boolean,
    params: Map[String, Any]
)

case class EmbulkConfiguration
(
  embulkPath: Path,
  bundleDirectory: Path,
  workingDirectory: Path,
  logFilePath: Path,
  registrations: Map[String, RegistrationConfiguration]
)

