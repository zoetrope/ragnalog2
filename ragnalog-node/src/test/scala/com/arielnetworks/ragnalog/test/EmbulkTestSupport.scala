package com.arielnetworks.ragnalog.test

import java.nio.file.Paths

import com.arielnetworks.ragnalog.port.adapter.embulk.{PluginConfiguration, RegistrationConfiguration}

trait EmbulkTestSupport {

  val userDir = System.getProperty("user.dir")
  val embulkPluginDir = Paths.get(System.getProperty("user.dir"), "etc/embulk/plugins/gems")
  val embulkWorkingDir = Paths.get(System.getProperty("user.dir"), "tmp/embulk/work")
  val embulkLogDir = Paths.get(System.getProperty("user.dir"), "tmp/embulk/log")
  val embulkLogFilePath = embulkLogDir.resolve("embulk.log")
  val grokPatternFilePath = embulkPluginDir.resolve("embulk-parser-grok-0.1.7").resolve("pattern").resolve("grok-patterns")

  val grokPluginConfig = PluginConfiguration(
    pluginType = "parser",
    className = "org.embulk.parser.grok.GrokParserPlugin",
    pluginName = "embulk-parser-grok-0.1.7",
    params = Map("patternFiles" -> List(grokPatternFilePath.toString))
  )
  val elasticsearchPluginConfig = PluginConfiguration(
    pluginType = "output",
    className = "org.embulk.output.elasticsearch.ElasticsearchOutputPlugin",
    pluginName = "embulk-output-elasticsearch-0.3.0",
    params = Map()
  )

  val apacheAccessConfig = RegistrationConfiguration(
    name = "Apache access log",
    description = None,
    parser = "grok",
    filters = Seq(),
    timeField = "timestamp",
    template = Paths.get(""),
    preprocessor = None,
    doGuess = false
  )
}
