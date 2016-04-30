package com.arielnetworks.ragnalog.port.adapter.embulk

import java.io.File
import java.nio.file.{Files, StandardCopyOption}

import com.arielnetworks.ragnalog.application._
import com.arielnetworks.ragnalog.domain.model.RegistrationService
import com.arielnetworks.ragnalog.support.ArchiveUtil

import scala.concurrent.Future
import scala.util.{Failure, Success}
import scalax.file.Path
import scalax.file.defaultfs.DefaultPath

class EmbulkAdapter(embulkConfiguration: EmbulkConfiguration) extends RegistrationService {

  val embulkSetting = embulkConfiguration
  val registrationsConfig = embulkSetting.registrations
  val embulkFacadeFactory = new EmbulkFacadeFactory(embulkConfiguration)
  val generator = new EmbulkYamlGenerator(embulkSetting.workingDirectory, embulkConfiguration.params)

  def register(command: RegistrationCommand): Future[RegistrationResult] = {
    try {
      val registrationConfig = registrationsConfig.get(command.logType).get //TODO:
      val archiveFilePath = command.archiveFileName

      //TODO: move to application layer
      val jfile: File = embulkSetting.workingDirectory match {
        case x: DefaultPath => x.jfile
      }
      val targetFile = File.createTempFile("temp", "log", jfile)
      targetFile.deleteOnExit()
      ArchiveUtil.getTargetStream(archiveFilePath, command.filePath).map(stream => {
        Files.copy(stream, targetFile.toPath, StandardCopyOption.REPLACE_EXISTING)
      }) //TODO: close stream, handle error

      // generate config file
      val generatedYamlPath = generator.generate(registrationConfig.template, Map(
        "indexName" -> command.indexName,
        "extra" -> command.extra,
        "input_file" -> targetFile
      ) ++ registrationConfig.params)

      //      logger.info("generated yaml: " + configPath);
      val embulkFacade = embulkFacadeFactory.create()

      // guess
      val configYamlPath = if (registrationConfig.doGuess) {
        val outputYaml = Path(File.createTempFile("guessed", ".yml"))
        embulkFacade.guess(generatedYamlPath, outputYaml, registrationConfig.guessPluginNames)
        //TODO: error check
        generatedYamlPath.deleteIfExists()
        outputYaml
      } else {
        generatedYamlPath
      }

      // run
      //      logger.info("embulk running...")
      val result = embulkFacade.run(configYamlPath)
      //      logger.info("embulk done.")

      targetFile.delete()

      result match {
        case Success(log) => Future.successful(new RegistrationResult(CommandSuccess(), generatedYamlPath, log))
        case Failure(e: CommandFailureException) => Future.successful(new RegistrationResult(CommandFailure(), generatedYamlPath, e.log))
        case Failure(e) => Future.failed(e)
      }

    } catch {
      case e: Throwable => {
        //logger.error(e)
        Future.failed(e)
      }
    }

  }
}
