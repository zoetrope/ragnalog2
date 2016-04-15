package com.arielnetworks.ragnalog.port.adapter.embulk

import java.io.File
import java.net.URL

import org.fusesource.scalate.TemplateEngine

class EmbulkYamlGenerator {

  def generate(uri: URL) = {
    val engine = new TemplateEngine
    engine.workingDirectory = new File("./tmp")

    val bindings =
      Map("name" -> "Scalate",
          "config" -> Map("hoge"->"Scala"))

    val output = engine.layout(uri.getPath, bindings)
    println(output)

  }
}
