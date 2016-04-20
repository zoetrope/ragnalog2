package com.arielnetworks.ragnalog.port.adapter.embulk

import java.nio.file.Paths

import com.arielnetworks.ragnalog.test.EmbulkTestSupport
import org.scalatest.{DiagrammedAssertions, FunSpec}

class EmbulkFacadeSpec extends FunSpec with DiagrammedAssertions with EmbulkTestSupport {

  val config = EmbulkConfiguration(
    embulkBinPath,
    embulkBundleDir,
    embulkWorkingDir,
    embulkLogFilePath,
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
  val accessConfig = config.registrations.get("apache.access").get
  val yaml = generator.generate(accessConfig.template, specificParams ++ accessConfig.params)
  val embulkFacade = new EmbulkFacade(config)

  describe("run") {
    describe("register apache access log") {

    }
    describe("parse error") {

    }
    describe("elasticsearch is down") {

    }
  }

  describe("guess") {

  }

  describe("plugins") {

    println("*********************************:")
    println(yaml)
    println("*********************************:")
    val result = embulkFacade.plugins()
    println(result)
    assert(result != null)
  }

  describe("invalid") {
    describe("invalid embulk path") {

      val invalidConfig = EmbulkConfiguration(
        Paths.get("invalid path"),
        embulkBundleDir,
        embulkWorkingDir,
        embulkLogFilePath,
        Map("apache.access" -> apacheAccessConfig)
      )

      val embulkFacade = new EmbulkFacade(invalidConfig)

      val result = embulkFacade.plugins()

      assert(result != null)
    }
    describe("invalid bundle dir") {

    }
    describe("plugin not found") {

    }
    describe("invalid yaml") {

    }
  }
}

