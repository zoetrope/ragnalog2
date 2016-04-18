package com.arielnetworks.ragnalog.port.adapter.embulk

import java.io.File
import java.nio.file.{Files, Paths}

import com.google.inject.{Binder, Guice, Module}
import org.embulk.EmbulkEmbed
import org.embulk.plugin.{InjectedPluginSource, PluginClassLoaderFactory, PluginClassLoaderModule}
import org.embulk.spi.{FilterPlugin, InputPlugin, OutputPlugin, ParserPlugin}

import scala.collection.JavaConversions._ //TODO: deprecated
import scala.util.{Failure, Success, Try}

object EmbulkEmbedFactory {

  val modules: Seq[Module] = Seq(new PluginClassLoaderModule(null))
  val injector = Guice.createInjector(modules)
  val factory = injector.getInstance(classOf[PluginClassLoaderFactory])

  def create(config: EmbulkConfiguration): Try[EmbulkEmbed] = {

    try {
      val bootstrap = new EmbulkEmbed.Bootstrap()
      configure(bootstrap, config)
      loadPlugin(bootstrap, config)
      Success(bootstrap.initialize())
    } catch {
      case e: Throwable => Failure(e)
    }
  }

  private def configure(bootstrap: EmbulkEmbed.Bootstrap, config: EmbulkConfiguration) = {
//    if (!Files.exists(config.logFilePath)) {
//      throw new IllegalArgumentException(s"${config.logFilePath} is not exist")
//    }

    val systemConfig = bootstrap.getSystemConfigLoader.newConfigSource
    systemConfig.set("log_path", config.logFilePath.toString)
    bootstrap.setSystemConfig(systemConfig)
  }

  private def loadPlugin(bootstrap: EmbulkEmbed.Bootstrap, config: EmbulkConfiguration) = {
    if (!Files.exists(config.pluginsDirectory)) {
      throw new IllegalArgumentException(s"${config.pluginsDirectory} is not exist")
    }

    for ((pluginName, plugin) <- config.plugins) {
      val classpath = config.pluginsDirectory.resolve(plugin.pluginName).resolve("classpath")
      if (!Files.exists(classpath)) {
        throw new IllegalArgumentException(s"$classpath is not exist")
      }

      val urls = recursiveListFiles(classpath.toFile).map(f => f.toURI.toURL).toList

      val pluginLoader = factory.create(urls, Thread.currentThread.getContextClassLoader)
      val pluginClass = pluginLoader.loadClass(plugin.className)
      bootstrap.addModules(new Module() {
        override
        def configure(binder: Binder) {
          InjectedPluginSource.registerPluginTo(
            binder,
            getPluginType(plugin.pluginType),
            pluginName,
            pluginClass
          )
        }
      })
    }
  }

  private def getPluginType(pluginType: String): Class[_] = {
    pluginType match {
      case "input" => classOf[InputPlugin]
      case "parser" => classOf[ParserPlugin]
      case "filter" => classOf[FilterPlugin]
      case "output" => classOf[OutputPlugin]
      case x => throw new IllegalArgumentException(s"$x is invalid plugin type")
    }
  }

  def recursiveListFiles(f: File): Array[File] = {
    val these = f.listFiles
    these ++ these.filter(_.isDirectory).flatMap(recursiveListFiles)
  }
}
