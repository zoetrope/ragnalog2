package com.arielnetworks.ragnalog.port.adapter.persistence.translator

import com.arielnetworks.ragnalog.domain.model.container.{Container, ContainerId, ContainerStatus}

trait ContainerTranslator extends Translator[ContainerId, Container] {

  protected def toFieldsFromEntity(container: Container): Map[String, Any] = {
    Map(
      "name" -> container.name,
      "description" -> container.description.getOrElse(""),
      "status" -> container.status.toString
    )
  }

  protected def toEntityFromFields(id: String, fields: java.util.Map[String, Object]): Container = {
    new Container(
      ContainerId(id),
      fields.get("name").asInstanceOf[String],
      Option(fields.get("description").asInstanceOf[String]),
      ContainerStatus.withName(fields.get("status").asInstanceOf[String])
    )
  }
}
