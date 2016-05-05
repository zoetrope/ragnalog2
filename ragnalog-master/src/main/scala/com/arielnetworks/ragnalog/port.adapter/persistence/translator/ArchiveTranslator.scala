package com.arielnetworks.ragnalog.port.adapter.persistence.translator

import com.arielnetworks.ragnalog.domain.model.archive.{Archive, ArchiveId, ArchiveType, LogFile}

import scalax.file.Path

trait ArchiveTranslator extends Translator[ArchiveId, Archive] {

  protected def toFieldsFromEntity(archive: Archive): Map[String, Any] = {
    Map(
      "fileName" -> archive.fileName,
      "filePath" -> archive.filePath.path,
      "archiveType" -> archive.archiveType.toString,
      "size" -> archive.size,
      "uploadedDate" -> fromTimeStamp(archive.uploadedDate),
      "modifiedDate" -> fromTimeStamp(archive.modifiedDate)
    )
  }

  protected def toEntityFromFields(id: String, fields: java.util.Map[String, Object]): Archive = {
    new Archive(
      ArchiveId(id),
      fields.get("fileName").asInstanceOf[String],
      Path(fields.get("filePath").asInstanceOf[String], '/'),
      ArchiveType.of(fields.get("archiveType").asInstanceOf[String]),
      fields.get("size").asInstanceOf[Int],
      toTimeStamp(fields.get("uploadedDate").asInstanceOf[String]),
      toTimeStamp(fields.get("modifiedDate").asInstanceOf[String])
    )
  }
}
