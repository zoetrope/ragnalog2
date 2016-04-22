package com.arielnetworks.ragnalog.port.adapter.embulk

import java.io._
import java.nio.file.Files

import scalax.file.Path
import com.arielnetworks.ragnalog.support.LoanSupport
import org.stringtemplate.v4.ST

import scala.io.{Codec, Source}
import scalax.file.defaultfs.DefaultPath


class EmbulkYamlGenerator(workDir: Path, baseParams: Map[String, Any]) extends LoanSupport {

  def generate(templateYamlPath: Path, specificParams: Map[String, Any]): Path = {
    val template = templateYamlPath.string
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

    workDir.createDirectory(failIfExists = false)
    val jfile: File = workDir match {
      case x: DefaultPath => x.jfile
    }
    val generatedYaml = File.createTempFile("temp", ".yml", jfile)
    println("---------------------")
    println(yaml)
    println("---------------------")
    using[Unit, PrintWriter](new PrintWriter(generatedYaml)) { writer =>
      writer.write(yaml)
    }
    Path(generatedYaml)
  }
}
