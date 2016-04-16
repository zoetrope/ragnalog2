package com.arielnetworks.ragnalog.port.adapter.embulk

import com.arielnetworks.ragnalog.test.CustomMatchers
import org.scalatest.Matchers._
import org.scalatest.{DiagrammedAssertions, FunSpec}

import scala.io.Source

class EmbulkYamlGeneratorSpec extends FunSpec with DiagrammedAssertions with CustomMatchers {

  describe("generate yaml") {

    val bindings = Map[String, Any](
      "input_file" -> "/tmp/ragnalog/sample.log",
      "extra" -> "ap1",
      "index_name" -> "ragnalog-test",
      "grok/grok_pattern_files" -> List("/home/ragnalog/grok-patterns"),
      "sar/sar_options" -> "-A",
      "sar/sadf_paths" -> Map(
        "9" -> "/opt/sysstat-9.0.6/sadf",
        "10" -> "/opt/sysstat-10.0.5/sadf",
        "11" -> "/opt/sysstat-11.0.7/sadf"
      ),
      "sar/exclude_fields" -> List("interrupts.*"),
      "elasticsearch/host" -> "localhost",
      "elasticsearch/port" -> "9300",
      "elasticsearch/cluster_name" -> "ragnalog2.elasticsearch"
    )
    val generator = new EmbulkYamlGenerator(bindings)

    describe("grok") {
      val uri = getClass.getClassLoader.getResource("template/grok.txt")
      val actual = generator.generate(uri)
      val expected = Source.fromURL(getClass.getClassLoader.getResource("expected/grok.yml")).getLines().mkString(System.lineSeparator())
      actual should sameYaml(expected)
    }
    describe("sar") {
      val uri = getClass.getClassLoader.getResource("template/sar.txt")
      val actual = generator.generate(uri)
      val expected = Source.fromURL(getClass.getClassLoader.getResource("expected/sar.yml")).getLines().mkString(System.lineSeparator())
      actual should sameYaml(expected)
    }
    describe("csv") {
      val uri = getClass.getClassLoader.getResource("template/csv.txt")
      val actual = generator.generate(uri)
      val expected = Source.fromURL(getClass.getClassLoader.getResource("expected/csv.yml")).getLines().mkString(System.lineSeparator())
      actual should sameYaml(expected)
    }
  }
}
