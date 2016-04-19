package com.arielnetworks.ragnalog.port.adapter.embulk

import java.io.File
import scala.sys.process.Process
import scala.language.postfixOps

import com.arielnetworks.ragnalog.application.RegistrationResult

class EmbulkFacade(config: EmbulkConfiguration, yaml: String) {


  def guess() = {
  }

  //TODO: Should this class have RegistrationResponse?
  def run(): RegistrationResult = {

    null
  }

  def plugins(): Seq[String] = {
    val ret = Process(config.embulkPath.toString + " gem list -b " + config.bundleDirectory.toString) !!

    println(ret)

    ret.split("\n").filter(_.startsWith("embulk-"))
  }
}

class EmbulkFacadeFactory(embulkConfiguration: EmbulkConfiguration) {

  def create(configPath: String): EmbulkFacade = new EmbulkFacade(embulkConfiguration, configPath)
}
