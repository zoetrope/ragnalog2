package com.arielnetworks.ragnalog.port.adapter.embulk

import java.nio.file.Paths

import com.arielnetworks.ragnalog.support.ElasticsearchTestSupport
import com.arielnetworks.ragnalog.test.EmbulkTestSupport
import org.scalatest.{BeforeAndAfterAll, DiagrammedAssertions, FunSpec}

import scala.util.{Failure, Success}

class EmbulkFacadeSpec extends FunSpec with DiagrammedAssertions with BeforeAndAfterAll
  with EmbulkTestSupport with ElasticsearchTestSupport {

  val testIndexName = "ragnalog-test-embulkfacade"
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
    "index_name" -> testIndexName
  )
  val generator = new EmbulkYamlGenerator(config.workingDirectory, baseParams)
  val accessConfig = config.registrations.get("apache.access").get
  val yaml = generator.generate(accessConfig.template, specificParams ++ accessConfig.params)
  val embulkFacade = new EmbulkFacade(config)

  override protected def afterAll() = {
    clearIndex(testIndexName)
  }

  describe("run") {
    describe("register apache access log") {
      val ret = embulkFacade.run(yaml)

      if(ret.isFailure){
        ret.failed.get.printStackTrace()
      }
      println("******************:")
      println(ret)
      println("******************:")

    }
    describe("parse error") {

    }
    describe("elasticsearch is down") {

    }
  }

  describe("guess") {

  }

  ignore("plugins") {
    it("should be c") {
      embulkFacade.plugins() match {
        case Success(r) =>
          assert(r.exists(p => p.name == "embulk-output-elasticsearch"))
          assert(r.exists(p => p.name == "embulk-parser-grok"))
          assert(r.exists(p => p.name == "embulk-filter-insert"))
        case Failure(e) => fail(e)
      }
    }
  }

  ignore("invalid") {
    describe("invalid embulk path") {

      val invalidConfig = EmbulkConfiguration(
        Paths.get("invalid path"),
        embulkBundleDir,
        embulkWorkingDir,
        embulkLogFilePath,
        Map("apache.access" -> apacheAccessConfig)
      )

      val embulkFacade = new EmbulkFacade(invalidConfig)
      embulkFacade.plugins()
    }
    describe("invalid bundle dir") {

    }
    describe("plugin not found") {

    }
    describe("invalid yaml") {

    }
  }
}

