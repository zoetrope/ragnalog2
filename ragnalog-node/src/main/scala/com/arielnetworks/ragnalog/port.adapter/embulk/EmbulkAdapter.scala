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
  //  val elasticsearchSetting = config.getElasticsearch

  val logTypesSetting = embulkSetting.types

  val embulkEmbed = EmbulkEmbedFactory.create(embulkSetting).get //TODO:

  //  val uploadedDir = config.getUploader().getUploadedDir

  val generator = new EmbulkYamlGenerator(Map())
  val preprocessors: Map[String, Preprocessor] = Map()

  def run(req: RegistrationRequest): Future[RegistrationResponse] = {
    try {
      val typeConfig = logTypesSetting.get(req.logType).get
      val archiveFilePath = req.archiveFileName

      var targetFile = File.createTempFile("temp", "log", new File(embulkSetting.temporaryPath))
      ArchiveUtil.getTargetStream(archiveFilePath, req.filePath).map(stream => {
        Files.copy(stream, targetFile.toPath, StandardCopyOption.REPLACE_EXISTING)
      }) //TODO: close stream, handle error

      val indexName = req.indexName

      // preprocess
      if (!typeConfig.preprocessor.isEmpty) {
        targetFile = preprocessors.get(typeConfig.preprocessor).map(p => p.preprocess(targetFile)).getOrElse(targetFile)
      }

      // generate config file
      //      val configPath = generator.generate(req.getLogType(), targetFile.toPath(), indexName, req.getExtra())
      val configPath = generator.generate(new URL(typeConfig.template), Map(
        "indexName" -> indexName,
        "extra" -> req.extra,
        "input_file" -> targetFile
      ))
      //      logger.info("generated yaml: " + configPath);
      val loader = embulkEmbed.newConfigLoader()
      val config = loader.fromYamlFile(new File(configPath))

      // guess
      if (typeConfig.doGuess) {
        val diff = embulkEmbed.guess(config)
        config.merge(diff)
      }

      //      logger.info(config)
      // run
      //      logger.info("embulk running...")
      val result = embulkEmbed.run(config)
      //      logger.info("embulk done.")
      val errorCount = result.getIgnoredExceptions.size()
      val res =
        if (result.getIgnoredExceptions.isEmpty) {
          new RegistrationResponse("", errorCount)
        } else {
          new RegistrationResponse(result.getIgnoredExceptions.size + " errors. first message: " + result.getIgnoredExceptions.get(0).getMessage, errorCount)
        }

      Future.successful(res)
    } catch {
      case e: Throwable => {
        //logger.error(e)
        Future.failed(e)
      }
    }

  }
}
