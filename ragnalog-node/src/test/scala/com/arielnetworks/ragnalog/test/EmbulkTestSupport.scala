package com.arielnetworks.ragnalog.test

import java.nio.file.Paths

import com.arielnetworks.ragnalog.port.adapter.embulk.RegistrationConfiguration

trait EmbulkTestSupport {

  val userDir = System.getProperty("user.dir")
  val embulkBinPath = Paths.get(System.getProperty("user.dir"), "etc/embulk/bin/embulk")
  val embulkBundleDir = Paths.get(System.getProperty("user.dir"), "etc/embulk")
  val embulkWorkingDir = Paths.get(System.getProperty("user.dir"), "tmp/embulk/work")
  val embulkLogDir = Paths.get(System.getProperty("user.dir"), "tmp/embulk/log")
  val embulkLogFilePath = embulkLogDir.resolve("embulk.log")
  val grokPatternFilePath = embulkBundleDir.resolve("embulk-parser-grok-0.1.7").resolve("pattern").resolve("grok-patterns")

  val grokTemplate = getClass.getClassLoader.getResource("template/access_without_filter.template")

  val apacheAccessConfig = RegistrationConfiguration(
    name = "Apache access log",
    description = None,
    parser = "grok",
    filters = Seq(),
    timeField = "timestamp",
    template = Paths.get(grokTemplate.getPath),
    preprocessor = None,
    doGuess = false,
      params = Map("grok/grok_pattern_files" -> List(grokPatternFilePath.toString))
  )
}
