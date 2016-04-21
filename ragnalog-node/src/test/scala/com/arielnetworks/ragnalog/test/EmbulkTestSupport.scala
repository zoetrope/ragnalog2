package com.arielnetworks.ragnalog.test

import scalax.file.Path

import com.arielnetworks.ragnalog.port.adapter.embulk.RegistrationConfiguration

trait EmbulkTestSupport {

  val userDir = System.getProperty("user.dir")
  val embulkBinPath = Path(System.getProperty("user.dir"), "etc", "embulk", "bin", "embulk")
  val embulkBundleDir = Path(System.getProperty("user.dir"), "etc", "embulk")
  val embulkPluginsDir = embulkBundleDir / Path("jruby", "2.2.0", "gems")
  val embulkWorkingDir = Path(System.getProperty("user.dir"), "tmp", "embulk", "work")
  val grokPatternFilePath = embulkPluginsDir / Path("embulk-parser-grok-0.1.7", "pattern", "grok-patterns")

  val grokTemplate = getClass.getClassLoader.getResource(Path("template", "access_without_filter.template").toString)

  val apacheAccessConfig = RegistrationConfiguration(
    name = "Apache access log",
    description = None,
    parser = "grok",
    filters = Seq(),
    timeField = "timestamp",
    template = Path(grokTemplate.getPath),
    preprocessor = None,
    doGuess = false,
    params = Map("grok/grok_pattern_files" -> List(grokPatternFilePath.toString))
  )
}
