package com.arielnetworks.ragnalog.port.adapter.embulk

import com.arielnetworks.ragnalog.test.CustomMatchers
import org.scalatest.Matchers._
import org.scalatest.{DiagrammedAssertions, FunSpec}

import scala.io.Source

class EmbulkYamlGeneratorSpec extends FunSpec with DiagrammedAssertions with CustomMatchers {

  describe("generate yaml") {
    describe("grok") {
      val generator = new EmbulkYamlGenerator
      val uri = getClass.getClassLoader.getResource("template/grok.mustache")
      assert(uri != null)
      val output = generator.generate(uri)
      val lines = Source.fromURL(getClass.getClassLoader.getResource("expected/grok.yml")).getLines().mkString(System.lineSeparator())

      output should sameYaml (lines)
    }
    describe("sar") {

    }
    describe("csv") {

    }
  }
}
