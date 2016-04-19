package com.arielnetworks.ragnalog.port.adapter.embulk

import com.arielnetworks.ragnalog.test.EmbulkTestSupport
import org.scalatest.{DiagrammedAssertions, FunSpec}

class EmbulkFacadeSpec extends FunSpec with DiagrammedAssertions with EmbulkTestSupport {

  val config = EmbulkConfiguration(
    embulkBinPath,
    embulkBundleDir,
    embulkWorkingDir,
    embulkLogFilePath,
    Map(
      "grok" -> grokPluginConfig,
      "elasticsearch" -> elasticsearchPluginConfig
    ),
    Map("apache.access" -> apacheAccessConfig)
  )

  val baseParams = Map[String, Any](
    "elasticsearch/host" -> "localhost",
    "elasticsearch/port" -> "9300",
    "elasticsearch/cluster_name" -> "ragnalog2.elasticsearch"
  )
  val specificParams = Map[String, Any](
    "input_file" -> getClass.getClassLoader.getResource("log/apache_access_100.log").getPath,
    "extra" -> "ap1",
    "index_name" -> "ragnalog-test-20160419"
  )
  val generator = new EmbulkYamlGenerator(baseParams)
  val regConfig = config.registrations.get("apache.access").get
  val grokConfig = config.plugins.get("grok").get
  val yaml = generator.generate(regConfig.template, specificParams ++ grokConfig.params)
  val embulkFacade = new EmbulkFacade(config, yaml)

  describe("run") {
    println("*********************************:")
    println(yaml)
    println("*********************************:")
    val result = embulkFacade.run()
    assert(result != null)
  }
}

