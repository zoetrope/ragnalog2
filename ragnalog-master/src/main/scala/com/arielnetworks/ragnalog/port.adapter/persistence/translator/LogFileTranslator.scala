package com.arielnetworks.ragnalog.port.adapter.persistence.translator

import com.arielnetworks.ragnalog.domain.model.archive.{LogFile, LogFileId, LogStatus}

trait LogFileTranslator  extends Translator[LogFileId, LogFile] {

  protected def toFieldsFromEntity(logFile: LogFile): Map[String, Any] = {
    Map(
      "filePath" -> logFile.filePath,
      "fileType" -> logFile.fileType.getOrElse(""),
      "status" -> logFile.status.toString,
      "index" -> logFile.index.getOrElse(""),
      "from" -> fromTimeStampOpt(logFile.from),
      "to" -> fromTimeStampOpt(logFile.to),
      "extra" -> logFile.extra.getOrElse(""),
      "count" -> logFile.count.getOrElse(""),
      "errorCount" -> logFile.errorCount.getOrElse(""),
      "errorMessage" -> logFile.errorMessage.getOrElse("")
    )
  }

  protected def toEntityFromFields(id: String, fields: java.util.Map[String, Object]): LogFile = {
    new LogFile(
      LogFileId(id),
      fields.get("filePath").asInstanceOf[String],
      Option(fields.get("fileType").asInstanceOf[String]),
      LogStatus.of(fields.get("archiveType").asInstanceOf[String]),
      Option(fields.get("index").asInstanceOf[String]),
      toTimeStampOpt(fields.get("from").asInstanceOf[String]),
      toTimeStampOpt(fields.get("to").asInstanceOf[String]),
      Option(fields.get("extra").asInstanceOf[String]),
      Option(fields.get("count").asInstanceOf[Long]),
      Option(fields.get("errorCount").asInstanceOf[Long]),
      Option(fields.get("errorMessage").asInstanceOf[String])
    )
  }
}
