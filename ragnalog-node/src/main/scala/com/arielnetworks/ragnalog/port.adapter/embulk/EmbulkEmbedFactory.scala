package com.arielnetworks.ragnalog.port.adapter.embulk

import java.io.File
import java.nio.file.Paths

import com.google.inject.{Binder, Guice, Module}
import org.embulk.EmbulkEmbed
import org.embulk.plugin.{InjectedPluginSource, PluginClassLoaderFactory, PluginClassLoaderModule}
import org.embulk.spi.{FilterPlugin, InputPlugin, OutputPlugin, ParserPlugin}

import scala.collection.JavaConversions._

object EmbulkEmbedFactory {

  val modules: Seq[Module] = Seq(new PluginClassLoaderModule(null))
  val injector = Guice.createInjector(modules)
  val factory = injector.getInstance(classOf[PluginClassLoaderFactory])

  def create(config: EmbulkConfiguration): Option[EmbulkEmbed] = {

    try {
      val bootstrap = new EmbulkEmbed.Bootstrap()
      val systemConfig = bootstrap.getSystemConfigLoader.newConfigSource

      systemConfig.set("log_path", config.logPath)
      bootstrap.setSystemConfig(systemConfig)

      for ((key, value) <- config.plugins) {

        val classpath = Paths.get(config.pluginsPath, value.fullName, "classpath")

        //TODO: need recover?
        val urls = recursiveListFiles(classpath.toFile).map(f => f.toURI.toURL).toList

        val pluginLoader = factory.create(urls, Thread.currentThread.getContextClassLoader)
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
      }
      Some(bootstrap.initialize())
    } catch {
      case e: Throwable => None
    }
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

  def recursiveListFiles(f: File): Array[File] = {
    val these = f.listFiles
    these ++ these.filter(_.isDirectory).flatMap(recursiveListFiles)
  }
}