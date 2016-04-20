package com.arielnetworks.ragnalog.port.adapter.embulk

import java.io.File

import scala.sys.process.Process
import scala.language.postfixOps
import com.arielnetworks.ragnalog.application.RegistrationResult

import scala.util.{Failure, Success, Try}

case class PluginInfo(name: String, versions: Seq[String])

class EmbulkFacade(config: EmbulkConfiguration) {

  val embulk = config.embulkPath
  val bundleDir = config.bundleDirectory


  def guess() = {
  }

  //TODO: Should this class have RegistrationResponse?
  def run(yaml: String): RegistrationResult = {

    null
  }

  def plugins(): Try[Seq[PluginInfo]] = {
    try {
      val ret = Process(s"$embulk gem list -b $bundleDir") !!
      val pluginNamePattern = """^(embulk-\S+) \((.*)\)$""".r
      Success(ret.split(System.lineSeparator()).collect({
        case pluginNamePattern(name, versions) => PluginInfo(name, versions.split(",").map(_.trim).toList)
      }))
    } catch {
      case e: Throwable => Failure(e)
    }
  }
}

class EmbulkFacadeFactory(embulkConfiguration: EmbulkConfiguration) {

  def create(configPath: String): EmbulkFacade = new EmbulkFacade(embulkConfiguration)
}
