package com.arielnetworks.ragnalog.port.adapter.persistence.translator

import com.arielnetworks.ragnalog.domain.model.common.{Entity, Identifier}
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat

trait Translator[ID <: Identifier[String], E <: Entity[ID]] {
  protected def toFieldsFromEntity(entity: E): Map[String, Any]

  protected def toEntityFromFields(id: String, fields: java.util.Map[String, Object]): E

  private val dateTimePattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"

  protected def fromTimeStamp(dateTime: DateTime): String = {
    dateTime.formatted(dateTimePattern)
  }

  protected def fromTimeStampOpt(dateTime: Option[DateTime]): String = {
    dateTime.map(f => f.formatted(dateTimePattern)).getOrElse("")
  }

  protected def toTimeStamp(value: String): DateTime = {
    DateTimeFormat.forPattern(dateTimePattern).parseDateTime(value)
  }

  protected def toTimeStampOpt(value: String): Option[DateTime] = {
    if (value != null) Some(DateTimeFormat.forPattern(dateTimePattern).parseDateTime(value)) else None
  }
}

