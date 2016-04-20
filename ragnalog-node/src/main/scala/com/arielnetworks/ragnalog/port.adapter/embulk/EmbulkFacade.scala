package com.arielnetworks.ragnalog.port.adapter.embulk

import java.io.File
import scala.sys.process.Process
import scala.language.postfixOps

import com.arielnetworks.ragnalog.application.RegistrationResult

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

  def plugins(): Seq[PluginInfo] = {
    val ret = Process(s"$embulk gem list -b $bundleDir") !!

    val pattern = """^(embulk-\S+) \((.*)\)$""".r
    ret.split(System.lineSeparator()).collect({
      case pattern(name, versions) => PluginInfo(name, versions.split(",").map(_.trim).toList)
    })
  }
}

class EmbulkFacadeFactory(embulkConfiguration: EmbulkConfiguration) {

  def create(configPath: String): EmbulkFacade = new EmbulkFacade(embulkConfiguration)
}
