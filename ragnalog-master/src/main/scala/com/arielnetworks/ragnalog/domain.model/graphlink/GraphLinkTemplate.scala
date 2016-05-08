package com.arielnetworks.ragnalog.domain.model.graphlink

import com.arielnetworks.ragnalog.domain.model.common.{Entity, Identifier}

case class GraphLinkTemplateId(id: String, parent: String = null) extends Identifier[String, String]

case class GraphLinkTemplate
(
  id: GraphLinkTemplateId,
  name: String,
  description: String,
  logType: String,
  template: String
) extends Entity[GraphLinkTemplateId]
