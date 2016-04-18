package com.arielnetworks.ragnalog.port.adapter.embulk

import com.arielnetworks.ragnalog.test.EmbulkTestSupport
import org.scalatest.{DiagrammedAssertions, FunSpec}

class EmbulkFacadeSpec extends FunSpec with DiagrammedAssertions with EmbulkTestSupport {

  val config = EmbulkConfiguration(
    embulkPluginDir,
    embulkWorkingDir,
    embulkLogFilePath,
    Map(
      "grok" -> grokPluginConfig,
      "elasticsearch" -> elasticsearchPluginConfig
    ),
    Map("apache.access" -> apacheAccessConfig)
  )

  val embulkEmbed = EmbulkEmbedFactory.create(config)
  val baseParams = Map[String, Any](
    "input_file" -> "/tmp/ragnalog/sample.log",
    "extra" -> "ap1",
    "index_name" -> "ragnalog-test",
    "elasticsearch/host" -> "localhost",
    "elasticsearch/port" -> "9300",
    "elasticsearch/cluster_name" -> "ragnalog2.elasticsearch"
  )
  val generator = new EmbulkYamlGenerator(baseParams)
//  val yaml = generator.generate()
//  val embulkFacade = new EmbulkFacade(embulkEmbed.get, yaml)

  describe(""){

  }
}

