package com.arielnetworks.ragnalog.port.adapter.embulk

case class PluginConfiguration
(
  typeName: String,
  className: String,
  fullName: String,
  params: Map[String, Any]
)

case class TypeConfiguration
(
  name: String,
  description: String,
  parser: String,
  filters: Seq[String],
  timeField: String,
  template: String,
  preprocessor: String,
  doGuess: Boolean
)

case class EmbulkConfiguration
(
  pluginsPath: String,
  logPath: String,
  temporaryPath: String,
  plugins: Map[String, PluginConfiguration],
  types: Map[String, TypeConfiguration]
)

