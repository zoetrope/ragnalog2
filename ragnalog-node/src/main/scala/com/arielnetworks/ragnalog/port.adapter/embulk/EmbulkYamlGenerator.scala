package com.arielnetworks.ragnalog.port.adapter.embulk

import java.io._
import java.nio.file.Path

import com.arielnetworks.ragnalog.support.LoanSupport
import org.stringtemplate.v4.ST

import scala.io.Source


class EmbulkYamlGenerator(workDir: Path, baseParams: Map[String, Any]) extends LoanSupport {

  def generate(templateYamlPath: Path, specificParams: Map[String, Any]): Path = {
    val template = Source.fromFile(templateYamlPath.toFile).getLines().mkString(System.lineSeparator())
    val st = new ST(template)

    val params = baseParams ++ specificParams

    //TODO: to recursive?
    params.foreach { case (key, value) =>
      value match {
        case items: List[_] => items.foreach(item => st.add(key, item))
        case itemMap: Map[_, _] => itemMap.foreach { case (k: String, v: Object) => st.addAggr(s"$key.{key, value}", k, v) }
        case _ => st.add(key, value)
      }
    }
    val yaml = st.render()

    val generatedYaml = File.createTempFile("temp", ".yml", workDir.toFile)
    using[Unit, PrintWriter](new PrintWriter(generatedYaml)) { writer =>
      writer.write(yaml)
    }
    generatedYaml.toPath
  }
}
