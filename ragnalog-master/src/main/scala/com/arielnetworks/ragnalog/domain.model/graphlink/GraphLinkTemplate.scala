package com.arielnetworks.ragnalog.domain.model.graphlink

import com.arielnetworks.ragnalog.domain.model.common.{Entity, Identifier}

case class GraphLinkTemplateId(value: String) extends Identifier[String]

case class GraphLinkTemplate
(
  id: GraphLinkTemplateId,
  name: String,
  description: String,
  logType: String,
  template: String
) extends Entity[GraphLinkTemplateId]
