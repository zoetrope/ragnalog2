package com.arielnetworks.ragnalog.port.adapter.embulk

import scalax.file.Path

import com.arielnetworks.ragnalog.test.CustomMatchers
import org.scalatest.Matchers._
import org.scalatest.{DiagrammedAssertions, FunSpec}

import scala.io.Source

class EmbulkYamlGeneratorSpec extends FunSpec with DiagrammedAssertions with CustomMatchers {

  describe("generate yaml") {

    val baseParams = Map[String, Any](
      "input_file" -> "/tmp/ragnalog/sample.log",
      "extra" -> "ap1",
      "index_name" -> "ragnalog-test",
      "elasticsearch/host" -> "localhost",
      "elasticsearch/port" -> "9300",
      "elasticsearch/cluster_name" -> "ragnalog2.elasticsearch"
    )
    val generator = new EmbulkYamlGenerator(Path(System.getProperty("user.dir"), '/') / Path("tmp", "embulk", "work"), baseParams)

    describe("grok") {
      val grokParams = Map(
        "grok/grok_pattern_files" -> List("/home/ragnalog/grok-patterns")
      )
      val uri = getClass.getClassLoader.getResource("template/grok.template")
      val actual = generator.generate(Path(uri.getPath, '/'), grokParams).string
      val expected = Source.fromURL(getClass.getClassLoader.getResource("expected/grok.yml")).getLines().mkString(System.lineSeparator())
      actual should sameYaml(expected)
    }
    describe("sar") {
      val sarParams = Map(
        "sar/sar_options" -> "-A",
        "sar/sadf_paths" -> Map(
          "9" -> "/opt/sysstat-9.0.6/sadf",
          "10" -> "/opt/sysstat-10.0.5/sadf",
          "11" -> "/opt/sysstat-11.0.7/sadf"
        ),
        "sar/exclude_fields" -> List("interrupts.*")
      )
      val uri = getClass.getClassLoader.getResource("template/sar.template")
      val actual = generator.generate(Path(uri.getPath, '/'), sarParams).string
      val expected = Source.fromURL(getClass.getClassLoader.getResource("expected/sar.yml")).getLines().mkString(System.lineSeparator())
      actual should sameYaml(expected)
    }
    describe("csv") {
      val uri = getClass.getClassLoader.getResource("template/csv.template")
      val actual = generator.generate(Path(uri.getPath, '/'), Map.empty).string
      val expected = Source.fromURL(getClass.getClassLoader.getResource("expected/csv.yml")).getLines().mkString(System.lineSeparator())
      actual should sameYaml(expected)
    }
  }
}
