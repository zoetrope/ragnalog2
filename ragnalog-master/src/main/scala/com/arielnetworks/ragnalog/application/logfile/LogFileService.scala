package com.arielnetworks.ragnalog.application.logfile

import com.arielnetworks.ragnalog.application.logfile.data.{GetLogFilesResponse, LogFileResponse, RegisterLogFileRequest, RegisterLogFileResponse}
import com.arielnetworks.ragnalog.domain.model.archive.ArchiveId
import com.arielnetworks.ragnalog.domain.model.logfile._
import com.arielnetworks.ragnalog.domain.model.registration.RegistrationService
import com.arielnetworks.ragnalog.domain.model.visualization.VisualizationAdapter
import com.arielnetworks.ragnalog.port.adapter.persistence.translator.TranslatorUtil

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success}


class LogFileService
(
  logFileRepository: LogFileRepository,
  registrationService: RegistrationService,
  visualizationService: VisualizationAdapter
) {

  def addLogFiles(logFiles: Seq[LogFile], archiveId: ArchiveId): Future[Unit] = {
    logFileRepository.addAll(logFiles, archiveId)
  }

  def removeAll(archiveId: ArchiveId): Future[Unit] = {
    for {
      count <- logFileRepository.countAll(None, Some(archiveId.id), None, None)
      logFiles <- logFileRepository.searchAll(0, count.asInstanceOf[Int], None, Some(archiveId.id), None, None)
      _ <- Future.sequence(logFiles.map(logFile => unregisterLogFile(logFile.id)))
      _ <- Future.sequence(logFiles.map(logFile => logFileRepository.deleteById(logFile.id)))
    } yield ()
  }

  def registerLogFile(requests: Seq[RegisterLogFileRequest]): Future[RegisterLogFileResponse] = {
    for {

      logFiles <- Future.sequence(requests.map(request => {
        for {
          logFile <- logFileRepository.resolveById(LogFileId(request.id, request.archiveId))
          updatedLogFile = logFile.startRegistering(request.logType, request.extra)
        } yield updatedLogFile
      }))

      _ <- logFileRepository.saveAll(logFiles)

    //      _ = registrationService.register(logFiles)

    } yield new RegisterLogFileResponse("ok")
  }

  def postRegistrationLogFile(id: LogFileId): Future[Unit] = {

    for {
      logFile <- logFileRepository.resolveById(id)
      //TODO:
      //      _ = if (ret) {
      //        _ <- visualizationService.activate(logFile)
      //        logFile.completeRegistering()
      //      } else {
      //        logFile.failToRegistering()
      //      }

      _ <- logFileRepository.save(logFile)
    } yield ()
  }


  def unregisterLogFile(id: LogFileId) = {
    for {
      logFile <- logFileRepository.resolveById(id)

      _ = registrationService.unregister(logFile)

      _ <- visualizationService.deactivate(logFile)

      _ <- logFileRepository.save(logFile)
    } yield ()

  }

  private val pageSize = 20

  def search(containerId: String, archiveId: Option[String], status: Option[String], name: Option[String], page: Int): Future[GetLogFilesResponse] = {
    for {
      count <- logFileRepository.countAll(Some(containerId), archiveId, status, name)
      logFiles <- logFileRepository.searchAll(page * pageSize, pageSize, Some(containerId), archiveId, status, name)
    } yield {
      new GetLogFilesResponse(
        logFiles.map(logFile => new LogFileResponse(
          logFile.id.id,
          logFile.id.parent,
          logFile.archiveName,
          logFile.logName,
          logFile.logType,
          logFile.status.toString,
          logFile.indexName,
          Option(TranslatorUtil.fromTimeStampOpt(logFile.from)),
          Option(TranslatorUtil.fromTimeStampOpt(logFile.to)),
          logFile.extra,
          logFile.count
        )),
        count,
        page
      )
    }
  }

}

