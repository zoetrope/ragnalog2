package com.arielnetworks.ragnalog.port.adapter.persistence.translator

import com.arielnetworks.ragnalog.domain.model.archive.{Archive, ArchiveId, ArchiveType}
import com.arielnetworks.ragnalog.domain.model.container.ContainerId

import scalax.file.Path

trait ArchiveTranslator extends Translator[ArchiveId, Archive] {

  override protected def toFieldsFromEntity(archive: Archive): Map[String, Any] = {
    Map(
      "fileName" -> archive.fileName,
      "filePath" -> archive.filePath.path,
      "size" -> archive.size,
      "uploadedDate" -> fromTimeStamp(archive.uploadedDate),
      "modifiedDate" -> fromTimeStamp(archive.modifiedDate),
      "fileNameEncoding" -> archive.fileNameEncoding
    )
  }

  override protected def toEntityFromFields(id: String, parent: String, fields: java.util.Map[String, Object]): Archive = {
    new Archive(
      ArchiveId(id, parent),
      fields.get("fileName").asInstanceOf[String],
      Path(fields.get("filePath").asInstanceOf[String], '/'),
      fields.get("size").asInstanceOf[Int],
      toTimeStamp(fields.get("uploadedDate").asInstanceOf[String]),
      toTimeStamp(fields.get("modifiedDate").asInstanceOf[String]),
      fields.get("fileNameEncoding").asInstanceOf[String]
    )
  }
}
