package com.arielnetworks.ragnalog.port.adapter.persistence.translator

import com.arielnetworks.ragnalog.domain.model.logfile.{LogFile, LogFileId, LogStatus}

import scalax.file.Path

trait LogFileTranslator extends Translator[LogFileId, LogFile] {

  protected def toFieldsFromEntity(logFile: LogFile): Map[String, Any] = {
    Map(
      "logName" -> logFile.logName,
      "logType" -> logFile.logType.getOrElse(""),
      "status" -> logFile.status.toString,
      "indexName" -> logFile.indexName.getOrElse(""),
      "from" -> fromTimeStampOpt(logFile.from),
      "to" -> fromTimeStampOpt(logFile.to),
      "extra" -> logFile.extra.getOrElse(""),
      "count" -> logFile.count.getOrElse(""),
      "registrationLog" -> fromPathOpt(logFile.registrationLog),
      "registrationSetting" -> fromPathOpt(logFile.registrationSetting)
    )
  }

  protected def toEntityFromFields(id: String, fields: java.util.Map[String, Object]): LogFile = {
    new LogFile(
      LogFileId(id),
      fields.get("logName").asInstanceOf[String],
      Option(fields.get("logType").asInstanceOf[String]),
      LogStatus.of(fields.get("archiveType").asInstanceOf[String]),
      Option(fields.get("indexName").asInstanceOf[String]),
      toTimeStampOpt(fields.get("from").asInstanceOf[String]),
      toTimeStampOpt(fields.get("to").asInstanceOf[String]),
      Option(fields.get("extra").asInstanceOf[String]),
      Option(fields.get("count").asInstanceOf[Long]),
      toPathOpt(fields.get("registrationLog").asInstanceOf[String]),
      toPathOpt(fields.get("registrationSetting").asInstanceOf[String])
    )
  }
}
