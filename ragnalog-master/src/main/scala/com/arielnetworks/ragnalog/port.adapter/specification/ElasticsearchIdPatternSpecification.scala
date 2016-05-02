package com.arielnetworks.ragnalog.port.adapter.specification

import com.arielnetworks.ragnalog.application.container.IdPatternSpecification

class ElasticsearchIdPatternSpecification  extends IdPatternSpecification{
  override def isSatisfied(id: String): Boolean = {
    id.matches("^([a-z0-9_]+)$")
  }
}
