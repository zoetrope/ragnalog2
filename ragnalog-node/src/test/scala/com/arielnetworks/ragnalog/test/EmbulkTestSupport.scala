package com.arielnetworks.ragnalog.test

import java.nio.file.Paths

import com.arielnetworks.ragnalog.port.adapter.embulk.RegistrationConfiguration

trait EmbulkTestSupport {

  val userDir = System.getProperty("user.dir")
  val embulkBinPath = Paths.get(System.getProperty("user.dir"), "etc", "embulk", "bin", "embulk")
  val embulkBundleDir = Paths.get(System.getProperty("user.dir"), "etc", "embulk")
  val embulkPluginsDir = embulkBundleDir.resolve(Paths.get("jruby", "2.2.0", "gems"))
  val embulkWorkingDir = Paths.get(System.getProperty("user.dir"), "tmp", "embulk", "work")
  val grokPatternFilePath = embulkPluginsDir.resolve(Paths.get("embulk-parser-grok-0.1.7", "pattern", "grok-patterns"))

  val grokTemplate = getClass.getClassLoader.getResource(Paths.get("template", "access_without_filter.template").toString)

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
