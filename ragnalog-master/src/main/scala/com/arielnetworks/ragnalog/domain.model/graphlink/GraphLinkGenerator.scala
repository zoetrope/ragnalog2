package com.arielnetworks.ragnalog.domain.model.graphlink

import com.arielnetworks.ragnalog.domain.model.logfile.LogFile
import com.arielnetworks.ragnalog.support.JodaTimeSupport._
import org.joda.time.DateTime
import org.stringtemplate.v4.ST

//TODO: Is this Domain Model?
class GraphLinkGenerator(hostName: String) {


  def generate(templates: Seq[GraphLinkTemplate], logFiles: Seq[LogFile]): Seq[GraphLink] = {
    val filesGroupedByLogType = logFiles.filter(f => f.fileType.isDefined).groupBy(_.fileType.get) //TODO

    templates.collect({
      case template if filesGroupedByLogType.contains(template.logType) => {
        val files = filesGroupedByLogType.get(template.logType).get //TODO
        val from = files.collect({ case d if d.from.isDefined => d.from.get }).min //TODO: need getOrElse?
        val to = files.collect({ case d if d.to.isDefined => d.to.get }).max
        val link = apply(template, files, from, to)
        val count = files.map(_.count.getOrElse(0L)).sum

        new GraphLink(link, from, to, template.name, template.logType, template.description, count)
      }
    })

  }

  private def apply(graphLinkTemplate: GraphLinkTemplate, files: Seq[LogFile], from: DateTime, to: DateTime): String = {

    val st = new ST(graphLinkTemplate.template)

    st.add("host_name", hostName)
    //    st.add("index_pattern", ) //TODO:
    st.add("from", from.formatted("YYYY-MM-DD`T`HH:mm:ss:nnnZ"))
    st.add("to", to.formatted("YYYY-MM-DD`T`HH:mm:ss:nnnZ"))

    st.render
  }
}
