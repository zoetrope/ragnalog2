package com.arielnetworks.ragnalog.port.adapter.embulk

import java.io.File
import java.net.URL
import java.nio.file.{Files, StandardCopyOption}

import com.arielnetworks.ragnalog.application.{RegistrationRequest, RegistrationResponse}
import com.arielnetworks.ragnalog.domain.model.RegistrationService
import com.arielnetworks.ragnalog.support.ArchiveUtil

import scala.concurrent.Future

class EmbulkAdapter(embulkConfiguration: EmbulkConfiguration) extends RegistrationService {

  val embulkSetting = embulkConfiguration
  val logTypesSetting = embulkSetting.types
  val embulkFacadeFactory = new EmbulkFacadeFactory(embulkConfiguration)

  //TODO: set base parameter
  val generator = new EmbulkYamlGenerator(Map())
  //TODO: set preprocessors
  val preprocessors: Map[String, Preprocessor] = Map()

  def run(req: RegistrationRequest): Future[RegistrationResponse] = {
    try {
      val typeConfig = logTypesSetting.get(req.logType).get //TODO:
      val archiveFilePath = req.archiveFileName

      //TODO: move to application layer
      var targetFile = File.createTempFile("temp", "log", new File(embulkSetting.temporaryPath))
      ArchiveUtil.getTargetStream(archiveFilePath, req.filePath).map(stream => {
        Files.copy(stream, targetFile.toPath, StandardCopyOption.REPLACE_EXISTING)
      }) //TODO: close stream, handle error

      // preprocess
      if (!typeConfig.preprocessor.isEmpty) {
        targetFile = preprocessors.get(typeConfig.preprocessor)
          .map(p => p.preprocess(targetFile))
          .getOrElse(targetFile)
      }

      // generate config file
      //TODO: build parameters
      val generatedYamlPath = generator.generate(new URL(typeConfig.template), Map(
        "indexName" -> req.indexName,
        "extra" -> req.extra,
        "input_file" -> targetFile
      ))
      //      logger.info("generated yaml: " + configPath);
      val embulkFacade = embulkFacadeFactory.create(generatedYamlPath)

      // guess
      if (typeConfig.doGuess) {
        embulkFacade.guess()
      }

      // run
      //      logger.info("embulk running...")
      val res = embulkFacade.run()
      //      logger.info("embulk done.")

      Future.successful(res)
    } catch {
      case e: Throwable => {
        //logger.error(e)
        Future.failed(e)
      }
    }

  }
}
