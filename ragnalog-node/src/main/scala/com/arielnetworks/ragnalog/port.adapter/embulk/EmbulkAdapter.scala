package com.arielnetworks.ragnalog.port.adapter.embulk

import java.io.{File, InputStream}
import java.net.URL
import java.nio.file.{Files, Path, Paths, StandardCopyOption}
import java.util.stream.Collectors

import com.arielnetworks.ragnalog.application.{RegistrationRequest, RegistrationResponse}
import com.arielnetworks.ragnalog.domain.model.RegistrationService
import com.arielnetworks.ragnalog.support.ArchiveUtil
import com.arielnetworks.ragnalog.support.LoanSupport._
import com.google.inject.{Binder, Guice, Module}
import org.embulk.EmbulkEmbed
import org.embulk.plugin.{InjectedPluginSource, PluginClassLoaderFactory, PluginClassLoaderModule}
import org.embulk.spi.{FilterPlugin, InputPlugin, OutputPlugin, ParserPlugin}

import scala.collection.JavaConversions._
import scala.concurrent.Future

class EmbulkAdapter(embulkConfiguration: EmbulkConfiguration) extends RegistrationService {

  val modules: Seq[Module] = Seq(new PluginClassLoaderModule(null))
  val injector = Guice.createInjector(modules)
  val factory = injector.getInstance(classOf[PluginClassLoaderFactory])

  val embulkSetting = embulkConfiguration
  //  val elasticsearchSetting = config.getElasticsearch

  val logTypesSetting = embulkSetting.types

  val embulkEmbed = prepare(embulkSetting)

  //  val uploadedDir = config.getUploader().getUploadedDir

  val generator = new EmbulkYamlGenerator(Map())
  val preprocessors: Map[String, Preprocessor] = Map()

  def recursiveListFiles(f: File): Array[File] = {
    val these = f.listFiles
    these ++ these.filter(_.isDirectory).flatMap(recursiveListFiles)
  }

  def prepare(config: EmbulkConfiguration): EmbulkEmbed = {

    val bootstrap = new EmbulkEmbed.Bootstrap()
    val systemConfig = bootstrap.getSystemConfigLoader.newConfigSource

    systemConfig.set("log_path", config.logPath)
    bootstrap.setSystemConfig(systemConfig)

    for ((key, value) <- config.plugins) {

      val classpath = Paths.get(config.pluginsPath, value.fullName, "classpath")

      //TODO: need recover?
      val urls = recursiveListFiles(classpath.toFile).map(f => f.toURI.toURL).toList

      val pluginLoader = factory.create(urls, Thread.currentThread.getContextClassLoader)
      try {
        val pluginClass = pluginLoader.loadClass(value.className)
        bootstrap.addModules(new Module() {
          override
          def configure(binder: Binder) {
            InjectedPluginSource.registerPluginTo(
              binder,
              getPluginType(value.typeName).get, //TODO
              key,
              pluginClass
            )
          }
        })
      } catch {
        case e: Throwable =>
      }
    }
    bootstrap.initialize()
  }

  private def getPluginType(pluginType: String): Option[Class[_]] = {
    pluginType match {
      case "input" => Some(classOf[InputPlugin])
      case "parser" => Some(classOf[ParserPlugin])
      case "filter" => Some(classOf[FilterPlugin])
      case "output" => Some(classOf[OutputPlugin])
      case _ => None
    }
  }

  def run(req: RegistrationRequest): Future[RegistrationResponse] = {
    try {
      val typeConfig = logTypesSetting.get(req.logType).get
      //      val generator = generatorMap.get(typeConfig.parser)

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
