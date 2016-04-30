package com.arielnetworks.ragnalog.port.adapter.embulk

import com.arielnetworks.ragnalog.support.ElasticsearchTestSupport
import com.arielnetworks.ragnalog.test.EmbulkTestSupport
import org.scalatest.{BeforeAndAfterAll, DiagrammedAssertions, FunSpec}

import scala.util.{Failure, Success}
import scalax.file.Path

class EmbulkFacadeSpec extends FunSpec with DiagrammedAssertions with BeforeAndAfterAll
  with EmbulkTestSupport with ElasticsearchTestSupport {

  val testIndexName = "ragnalog-test-embulkfacade"
  val config = EmbulkConfiguration(
    embulkBinPath,
    embulkBundleDir,
    embulkWorkingDir,
    Map("apache.access" -> apacheAccessConfig),
    baseParams
  )
  val specificParams = Map[String, Any](
    "input_file" -> getClass.getClassLoader.getResource("log/apache_access_100.log").getPath,
    "extra" -> "ap1",
    "index_name" -> testIndexName
  )
  val generator = new EmbulkYamlGenerator(config.workingDirectory, config.params)
  val accessConfig = config.registrations.get("apache.access").get
  val yaml = generator.generate(accessConfig.template, specificParams ++ accessConfig.params)
  val embulkFacade = new EmbulkFacade(config)


  override protected def afterAll() = {
    //    clearIndex(testIndexName)
  }

  describe("run") {
    describe("register apache access log") {
      it("should be registered in Elasticsearc") {
        val ret = embulkFacade.run(yaml)

        ret match {
          case Success(bytes) =>
            val log = decodeZip(bytes)
            println("******************:")
            println(log)
            println("******************:")
          case Failure(e: CommandFailureException) =>
            val log = decodeZip(e.log)
            println("******************:")
            println(log)
            println("******************:")
          case Failure(ex) => ex.printStackTrace()
        }
      }
    }

    describe("parse error") {

    }
    describe("elasticsearch is down") {

    }
    describe("unknown grok pattern") {

    }
  }

  describe("guess") {

  }

  ignore("invalid") {
    describe("invalid embulk path") {

      val invalidConfig = EmbulkConfiguration(
        Path("invalid path"),
        embulkBundleDir,
        embulkWorkingDir,
        Map("apache.access" -> apacheAccessConfig),
        baseParams
      )

      val embulkFacade = new EmbulkFacade(invalidConfig)
      embulkFacade.run(yaml)
    }
    describe("invalid bundle dir") {

    }
    describe("plugin not found") {

    }
    describe("invalid yaml") {

    }
  }
}

