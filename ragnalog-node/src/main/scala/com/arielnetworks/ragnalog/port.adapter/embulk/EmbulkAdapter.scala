package com.arielnetworks.ragnalog.port.adapter.embulk

import java.io.File
import java.net.URL
import java.nio.file.{Files, StandardCopyOption}

import com.arielnetworks.ragnalog.application.{RegistrationCommand, RegistrationResult}
import com.arielnetworks.ragnalog.domain.model.RegistrationService
import com.arielnetworks.ragnalog.support.ArchiveUtil

import scala.concurrent.Future

class EmbulkAdapter(embulkConfiguration: EmbulkConfiguration) extends RegistrationService {

  val embulkSetting = embulkConfiguration
  val registrationsConfig = embulkSetting.registrations
  val embulkFacadeFactory = new EmbulkFacadeFactory(embulkConfiguration)

  //TODO: set base parameter
  val generator = new EmbulkYamlGenerator(embulkSetting.workingDirectory, Map())
  //TODO: set preprocessors
  val preprocessors: Map[String, Preprocessor] = Map()

  def run(command: RegistrationCommand): Future[RegistrationResult] = {
    try {
      val registrationConfig = registrationsConfig.get(command.logType).get //TODO:
      val archiveFilePath = command.archiveFileName

      //TODO: move to application layer
      var targetFile = File.createTempFile("temp", "log", embulkSetting.workingDirectory.toFile)
      ArchiveUtil.getTargetStream(archiveFilePath, command.filePath).map(stream => {
        Files.copy(stream, targetFile.toPath, StandardCopyOption.REPLACE_EXISTING)
      }) //TODO: close stream, handle error

      // preprocess
      targetFile = registrationConfig.preprocessor
        .flatMap(preprocessor => preprocessors.get(preprocessor))
        .map(p => p.preprocess(targetFile))
        .getOrElse(targetFile)

      // generate config file
      //TODO: build parameters
      val generatedYamlPath = generator.generate(registrationConfig.template, Map(
        "indexName" -> command.indexName,
        "extra" -> command.extra,
        "input_file" -> targetFile
      ))
      //      logger.info("generated yaml: " + configPath);
      val embulkFacade = embulkFacadeFactory.create()

      // guess
      if (registrationConfig.doGuess) {
        embulkFacade.guess()
      }

      // run
      //      logger.info("embulk running...")
      val result = embulkFacade.run(generatedYamlPath)
      //      logger.info("embulk done.")

      Future.successful(new RegistrationResult("", 0))
    } catch {
      case e: Throwable => {
        //logger.error(e)
        Future.failed(e)
      }
    }

  }
}
