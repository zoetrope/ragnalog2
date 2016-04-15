package com.arielnetworks.ragnalog.port.adapter.embulk

import org.scalatest.concurrent.ScalaFutures
import org.scalatest.{DiagrammedAssertions, FunSpec}

class EmbulkYamlGeneratorSpec extends FunSpec with DiagrammedAssertions with ScalaFutures {

  describe("generate yaml") {
    val generator = new EmbulkYamlGenerator
    val uri = getClass.getClassLoader.getResource("mustache/sample.mustache")
    assert(uri != null)
    generator.generate(uri)
  }
}
