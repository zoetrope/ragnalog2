package com.arielnetworks.ragnalog.port.adapter.embulk

import java.nio.file.Path

case class PluginConfiguration
(
  pluginType: String, //TODO: to enum?
  className: String,
  pluginName: String,
  params: Map[String, Any]
)

case class RegistrationConfiguration
(
  name: String,
  description: Option[String],
  parser: String,
  filters: Seq[String],
  timeField: String,
  template: Path,
  preprocessor: Option[String],
  doGuess: Boolean
)

case class EmbulkConfiguration
(
  pluginsDirectory: Path,
  workingDirectory: Path,
  logFilePath: Path,
  plugins: Map[String, PluginConfiguration],
  registrations: Map[String, RegistrationConfiguration]
)

