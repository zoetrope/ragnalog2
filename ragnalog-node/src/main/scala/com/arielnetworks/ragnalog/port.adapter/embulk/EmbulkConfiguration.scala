package com.arielnetworks.ragnalog.port.adapter.embulk

import org.embulk.plugin.PluginType

case class PluginConfiguration
(
  pluginType: String, //TODO:
  className: String,
  pluginName: String,
  params: Map[String, Any]
)

case class RegistrationConfiguration
(
  logType: String,
  description: Option[String],
  parser: String,
  filters: Seq[String],
  timeField: String,
  template: String,
  preprocessor: Option[String],
  doGuess: Boolean
)

case class EmbulkConfiguration
(
  pluginsPath: String,
  logPath: String,
  temporaryPath: String,
  plugins: Map[String, PluginConfiguration],
  registrations: Map[String, RegistrationConfiguration]
)

