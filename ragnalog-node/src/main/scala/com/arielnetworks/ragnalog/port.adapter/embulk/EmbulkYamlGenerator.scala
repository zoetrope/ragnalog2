package com.arielnetworks.ragnalog.port.adapter.embulk

import java.io.File
import java.net.URL

import org.fusesource.scalate.TemplateEngine

class EmbulkYamlGenerator(bindings: Map[String, Any]) {

  def generate(uri: URL): String = {
    val engine = new TemplateEngine
    engine.workingDirectory = new File("./tmp")
    engine.layout(uri.getPath, bindings)
  }
}
