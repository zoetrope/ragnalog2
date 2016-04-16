package com.arielnetworks.ragnalog.port.adapter.embulk

import java.net.URL

import org.stringtemplate.v4.ST

import scala.io.Source


class EmbulkYamlGenerator(bindings: Map[String, Any]) {

  def generate(uri: URL): String = {
    val template = Source.fromURL(uri).getLines().mkString(System.lineSeparator())
    val st = new ST(template)
    bindings.foreach { case (key, value) =>
      value match {
        case items: List[_] => items.foreach(item => st.add(key, item))
        case itemMap: Map[_, _] => itemMap.foreach { case (k: String, v: Object) => st.addAggr(s"$key.{key, value}", k, v) }
        case _ => st.add(key, value)
      }
    }
    st.render()
  }
}
