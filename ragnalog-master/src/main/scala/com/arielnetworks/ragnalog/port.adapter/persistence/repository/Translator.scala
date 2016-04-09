package com.arielnetworks.ragnalog.port.adapter.persistence.repository

import com.arielnetworks.ragnalog.domain.model.common.{Entity, Identifier}
import com.arielnetworks.ragnalog.domain.model.container.{Container, ContainerId}

trait Translator[ID <: Identifier[String], E <: Entity[ID]] {
  protected def toFieldsFromEntity(entity: E): Iterable[(String, Any)]
}

trait ContainerTranslator extends Translator[ContainerId, Container] {

  protected def toFieldsFromEntity(container: Container): Iterable[(String, Any)] = {
    List(
      "name" -> container.name,
      "description" -> container.description.getOrElse("")
    )
  }

}
