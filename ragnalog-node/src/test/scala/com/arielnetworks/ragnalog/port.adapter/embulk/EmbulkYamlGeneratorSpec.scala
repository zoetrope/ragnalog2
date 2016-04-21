package com.arielnetworks.ragnalog.port.adapter.embulk

import java.nio.file.Paths

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
    val generator = new EmbulkYamlGenerator(Paths.get(System.getProperty("user.dir"), "tmp/embulk/work"), baseParams)

    describe("grok") {
      val grokParams = Map(
        "grok/grok_pattern_files" -> List("/home/ragnalog/grok-patterns")
      )
      val uri = getClass.getClassLoader.getResource("template/grok.template")
      val actual = Source.fromFile(generator.generate(Paths.get(uri.getPath), grokParams).toFile).getLines().mkString(System.lineSeparator())
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
      val actual = Source.fromFile(generator.generate(Paths.get(uri.getPath), sarParams).toFile).getLines().mkString(System.lineSeparator())
      val expected = Source.fromURL(getClass.getClassLoader.getResource("expected/sar.yml")).getLines().mkString(System.lineSeparator())
      actual should sameYaml(expected)
    }
    describe("csv") {
      val uri = getClass.getClassLoader.getResource("template/csv.template")
      val actual = Source.fromFile(generator.generate(Paths.get(uri.getPath), Map.empty).toFile).getLines().mkString(System.lineSeparator())
      val expected = Source.fromURL(getClass.getClassLoader.getResource("expected/csv.yml")).getLines().mkString(System.lineSeparator())
      actual should sameYaml(expected)
    }
  }
}
