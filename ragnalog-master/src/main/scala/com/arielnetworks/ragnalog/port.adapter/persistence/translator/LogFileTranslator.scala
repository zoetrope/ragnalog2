package com.arielnetworks.ragnalog.port.adapter.persistence.translator

import com.arielnetworks.ragnalog.domain.model.container.ContainerId
import com.arielnetworks.ragnalog.domain.model.logfile.{LogFile, LogFileId, LogStatus}

import scalax.file.Path

trait LogFileTranslator extends Translator[LogFileId, LogFile] {

  protected def toFieldsFromEntity(logFile: LogFile): Map[String, Any] = {
    Map(
      "containerId" -> logFile.containerId.value,
      "archiveName" -> logFile.archiveName,
      "logName" -> logFile.logName,
      "logType" -> logFile.logType.orNull,
      "status" -> logFile.status.toString,
      "indexName" -> logFile.indexName.orNull,
      "from" -> fromTimeStampOpt(logFile.from),
      "to" -> fromTimeStampOpt(logFile.to),
      "extra" -> logFile.extra.orNull,
      "count" -> logFile.count.orNull,
      "registrationLog" -> fromPathOpt(logFile.registrationLog),
      "registrationSetting" -> fromPathOpt(logFile.registrationSetting)
    )
  }

  protected def toEntityFromFields(id: String, fields: java.util.Map[String, Object]): LogFile = {
    new LogFile(
      LogFileId(id),
      ContainerId(fields.get("containerId").asInstanceOf[String]),
      fields.get("archiveName").asInstanceOf[String],
      fields.get("logName").asInstanceOf[String],
      Option(fields.get("logType").asInstanceOf[String]),
      LogStatus.of(fields.get("status").asInstanceOf[String]),
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
