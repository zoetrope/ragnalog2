package com.arielnetworks.ragnalog.domain.model.graphlink

import org.joda.time.DateTime

//TODO: Is this an entity?
case class GraphLink
(
  link: String,
  from: DateTime,
  to: DateTime,
  name: String,
  logType: String,
  description: String,
  count: Long
)

