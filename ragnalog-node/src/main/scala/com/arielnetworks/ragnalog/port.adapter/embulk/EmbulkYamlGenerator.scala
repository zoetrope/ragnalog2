package com.arielnetworks.ragnalog.port.adapter.embulk

import java.nio.file.Path

import org.stringtemplate.v4.ST

import scala.io.Source


class EmbulkYamlGenerator(baseParams: Map[String, Any]) {

  def generate(templateYamlPath: Path, specificParams: Map[String, Any]): String = {
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
    st.render()
  }
}
