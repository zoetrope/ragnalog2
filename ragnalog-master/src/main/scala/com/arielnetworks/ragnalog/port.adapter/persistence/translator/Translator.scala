package com.arielnetworks.ragnalog.port.adapter.persistence.translator

import com.arielnetworks.ragnalog.domain.model.common.{Entity, Identifier}
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat

import scalax.file.Path

trait Translator[ID <: Identifier[String], E <: Entity[ID]] {
  protected def toFieldsFromEntity(entity: E): Map[String, Any]

  //TODO: parent
  protected def toEntityFromFields(id: String, fields: java.util.Map[String, Object]): E

  private val dateTimePattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZZ"

  protected def fromTimeStamp(dateTime: DateTime): String = {
    DateTimeFormat.forPattern(dateTimePattern).print(dateTime)
  }

  protected def fromTimeStampOpt(dateTime: Option[DateTime]): String = {
    dateTime.map(f => DateTimeFormat.forPattern(dateTimePattern).print(f)).orNull
  }

  protected def toTimeStamp(value: String): DateTime = {
    DateTimeFormat.forPattern(dateTimePattern).parseDateTime(value)
  }

  protected def toTimeStampOpt(value: String): Option[DateTime] = {
    if (value != null) Some(DateTimeFormat.forPattern(dateTimePattern).parseDateTime(value)) else None
  }

  protected def fromPathOpt(value: Option[Path]): String = {
    value.map(_.path).orNull
  }

  protected def toPathOpt(value: String): Option[Path] = {
    if (value != null) Some(Path(value, '/')) else None
  }
}

