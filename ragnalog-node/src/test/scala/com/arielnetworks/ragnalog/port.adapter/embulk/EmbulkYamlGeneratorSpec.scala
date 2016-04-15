package com.arielnetworks.ragnalog.port.adapter.embulk

import org.scalatest.concurrent.ScalaFutures
import org.scalatest.{DiagrammedAssertions, FunSpec}
import org.yaml.snakeyaml.Yaml
import org.scalactic._
import TripleEquals._
import StringNormalizations._
import Explicitly._
import scala.io.Source

class EmbulkYamlGeneratorSpec extends FunSpec with DiagrammedAssertions with ScalaFutures {

  describe("generate yaml") {
    describe("grok"){
      val generator = new EmbulkYamlGenerator
      val uri = getClass.getClassLoader.getResource("template/grok.mustache")
      assert(uri != null)
      val output = generator.generate(uri)
      val lines = Source.fromURL(getClass.getClassLoader.getResource("expected/grok.yml")).getLines().mkString(System.lineSeparator())
      val expect = new Yaml().load(lines)
      val actual = new Yaml().load(output)
//      assert(expect == actual)
      assert(expect === actual)
    }
    describe("sar"){

    }
    describe("csv"){

    }
  }
}
