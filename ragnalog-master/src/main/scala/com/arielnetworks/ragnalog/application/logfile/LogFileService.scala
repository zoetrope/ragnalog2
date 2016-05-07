package com.arielnetworks.ragnalog.application.logfile

import com.arielnetworks.ragnalog.application.logfile.data.{GetLogFilesResponse, LogFileResponse, RegisterLogFileRequest}
import com.arielnetworks.ragnalog.domain.model.archive.ArchiveId
import com.arielnetworks.ragnalog.domain.model.logfile._
import com.arielnetworks.ragnalog.domain.model.registration.RegistrationService
import com.arielnetworks.ragnalog.domain.model.visualization.VisualizationAdapter

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

  def removeAll(id: String): Future[Unit] = {
    val archiveId = ArchiveId(id)
    for {
      count <- logFileRepository.countAll(None, Some(archiveId.value), None, None)
      logFiles <- logFileRepository.searchAll(0, count.asInstanceOf[Int], None, Some(archiveId.value), None, None)
      _ <- Future.sequence(logFiles.map(logFile => unregisterLogFile(logFile.id.value)))
      _ <- Future.sequence(logFiles.map(logFile => logFileRepository.deleteById(logFile.id)))
    } yield ()
  }

  def registerLogFile(req: RegisterLogFileRequest) = {
    for {
      logFile <- logFileRepository.resolveById(LogFileId(req.id))

      _ = logFile.startRegistering(req.logType, req.extra)

      _ <- logFileRepository.save(logFile)

      _ = registrationService.register(logFile)

    } yield ()
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


  def unregisterLogFile(id: String) = {
    for {
      logFile <- logFileRepository.resolveById(LogFileId(id))

      _ = registrationService.unregister(logFile)

      _ <- visualizationService.deactivate(logFile)

      _ <- logFileRepository.save(logFile)
    } yield ()

  }

  private val pageSize = 20

  def search(containerId: String, archiveId: Option[String], status: Option[String], name: Option[String], page: Int): Future[GetLogFilesResponse] = {
    println(s"search: $containerId, $archiveId, $status, $name, $page")
    //TODO: pagination
    for {
      count <- logFileRepository.countAll(Some(containerId), archiveId, status, name)
      logFiles <- logFileRepository.searchAll(page * pageSize, pageSize, Some(containerId), archiveId, status, name)
    } yield {
      new GetLogFilesResponse(
        logFiles.map(logFile => new LogFileResponse(
          logFile.id.value,
          logFile.archiveName,
          logFile.logName,
          logFile.logType,
          logFile.status.toString,
          logFile.indexName,
          logFile.from.map(_.toString), //TODO: format
          logFile.to.map(_.toString), //TODO: format
          logFile.extra,
          logFile.count
        )),
        count,
        page
      )
    }
  }

}

