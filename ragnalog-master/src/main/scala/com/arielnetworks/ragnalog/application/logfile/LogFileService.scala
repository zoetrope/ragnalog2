package com.arielnetworks.ragnalog.application.logfile

import com.arielnetworks.ragnalog.application.logfile.data.RegisterLogFileRequest
import com.arielnetworks.ragnalog.domain.model.archive.ArchiveId
import com.arielnetworks.ragnalog.domain.model.logfile._
import com.arielnetworks.ragnalog.domain.model.registration.RegistrationAdapter
import com.arielnetworks.ragnalog.domain.model.visualization.VisualizationAdapter
import scala.concurrent.ExecutionContext.Implicits.global

import scala.concurrent.Future


class LogFileService
(
  logFileRepository: LogFileRepository,
  registrationService: RegistrationAdapter,
  visualizationService: VisualizationAdapter
) {

  def addLogFiles(logFiles: Seq[LogFile]) = {
    Future.sequence(
      logFiles.map(logFile => logFileRepository.add(logFile))
    )
  }

  def registerLogFile(req: RegisterLogFileRequest) = {
    for {
      logFile <- logFileRepository.resolveById(LogFileId(req.id))

      _ = logFile.update(req.fileType, req.extra, Registering)

      _ <- logFileRepository.save(logFile)

      _ <- registrationService.register(logFile)

      _ <- visualizationService.activate(logFile)

      _ <- logFileRepository.save(logFile)
    } yield ()
  }


  def unregisterLogFile(id: String) = {
    for {
      logFile <- logFileRepository.resolveById(LogFileId(id))

      _ <- registrationService.unregister(logFile)

      _ <- visualizationService.deactivate(logFile)

      _ <- logFileRepository.save(logFile)
    } yield ()

  }

  def removeAll(id: String): Future[Unit] = {
    val archiveId = ArchiveId(id)
    for {
      count <- logFileRepository.countAll(archiveId)
      logFiles <- logFileRepository.searchAll(0, count.asInstanceOf[Int], archiveId)
      _ <- Future.sequence(logFiles.map(logFile => unregisterLogFile(logFile.id.value)))
      _ <- Future.sequence(logFiles.map(logFile => logFileRepository.deleteById(logFile.id)))
    } yield ()
  }
}

