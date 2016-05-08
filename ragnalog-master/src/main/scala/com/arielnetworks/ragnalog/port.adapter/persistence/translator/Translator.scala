package com.arielnetworks.ragnalog.port.adapter.persistence.translator

import com.arielnetworks.ragnalog.domain.model.common.{Entity, Identifier}
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat

import scalax.file.Path

trait Translator[ID <: Identifier[String, String], E <: Entity[ID]] extends TranslatorUtil {
  protected def toFieldsFromEntity(entity: E): Map[String, Any]

  protected def toEntityFromFields(id: String, parent: String, fields: java.util.Map[String, Object]): E
}

trait TranslatorUtil {
  private val dateTimePattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZZ"

  def fromTimeStamp(dateTime: DateTime): String = {
    DateTimeFormat.forPattern(dateTimePattern).print(dateTime)
  }

  def fromTimeStampOpt(dateTime: Option[DateTime]): String = {
    dateTime.map(f => DateTimeFormat.forPattern(dateTimePattern).print(f)).orNull
  }

  def toTimeStamp(value: String): DateTime = {
    DateTimeFormat.forPattern(dateTimePattern).parseDateTime(value)
  }

  def toTimeStampOpt(value: String): Option[DateTime] = {
    if (value != null) Some(DateTimeFormat.forPattern(dateTimePattern).parseDateTime(value)) else None
  }

  def fromPathOpt(value: Option[Path]): String = {
    value.map(_.path).orNull
  }

  def toPathOpt(value: String): Option[Path] = {
    if (value != null) Some(Path(value, '/')) else None
  }
}

object TranslatorUtil extends TranslatorUtil
