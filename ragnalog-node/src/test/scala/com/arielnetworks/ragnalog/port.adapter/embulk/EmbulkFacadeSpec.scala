package com.arielnetworks.ragnalog.port.adapter.embulk

import java.io.File

import com.arielnetworks.ragnalog.test.{CustomMatchers, EmbulkTestSupport}
import org.scalatest.Matchers._
import org.scalatest.{BeforeAndAfterAll, DiagrammedAssertions, FunSpec}

import scala.io.Source
import scala.util.{Failure, Success}
import scalax.file.Path

class EmbulkFacadeSpec extends FunSpec with DiagrammedAssertions with BeforeAndAfterAll
  with EmbulkTestSupport with CustomMatchers {

  val testIndexName = "ragnalog-test-embulkfacade"
  val config = EmbulkConfiguration(
    embulkBinPath,
    embulkBundleDir,
    embulkWorkingDir,
    Map("apache.access" -> apacheAccessConfig),
    baseParams
  )
  val generator = new EmbulkYamlGenerator(config.workingDirectory, config.params)
  val embulkFacade = new EmbulkFacade(config)


  override protected def afterAll() = {
    //    clearIndex(testIndexName)
  }

  ignore("run") {
    describe("register apache access log") {
      it("should be registered to Elasticsearch") {
        val specificParams = Map[String, Any](
          "input_file" -> getClass.getClassLoader.getResource("log/apache_access_100.log").getPath,
          "extra" -> "ap1",
          "index_name" -> testIndexName
        )
        val yaml = generator.generate(grokTemplate, specificParams ++ grokParams)

        val ret = embulkFacade.run(yaml)

        yaml.delete()

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
            fail(e)
          case Failure(ex) =>
            ex.printStackTrace()
            fail(ex)
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
    it("should be guessed grok pattern") {
      val specificParams = Map[String, Any](
        "input_file" -> getClass.getClassLoader.getResource("log/apache_access_100.log").getPath,
        "extra" -> "ap1",
        "index_name" -> testIndexName
      )
      val partialYaml = generator.generate(grokGuessTemplate, specificParams ++ grokParams)
      val outputYaml = File.createTempFile("guessed", ".yml")
//      outputYaml.deleteOnExit()

      val ret = embulkFacade.guess(partialYaml, Path(outputYaml), Some(Seq("grok")))

      partialYaml.delete()

      ret match {
        case Success(bytes) =>
          val log = decodeZip(bytes)
          println("******************:")
          println(log)
          println("******************:")
          val actual = Source.fromFile(outputYaml).getLines().mkString(System.lineSeparator())
          val expected = Source.fromURL(getClass.getClassLoader.getResource("expected/grok_guessed.yml")).getLines().mkString(System.lineSeparator())
          actual should sameYaml(expected, Set("path_prefix", "grok_pattern_files"))
        case Failure(e: CommandFailureException) =>
          val log = decodeZip(e.log)
          println("******************:")
          println(log)
          println("******************:")
          fail(e)
        case Failure(ex) =>
          ex.printStackTrace()
          fail(ex)
      }
    }

  }

  ignore("invalid") {
    describe("invalid embulk path") {

      val specificParams = Map[String, Any](
        "input_file" -> getClass.getClassLoader.getResource("log/apache_access_100.log").getPath,
        "extra" -> "ap1",
        "index_name" -> testIndexName
      )
      val yaml = generator.generate(grokTemplate, specificParams ++ grokParams)

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

