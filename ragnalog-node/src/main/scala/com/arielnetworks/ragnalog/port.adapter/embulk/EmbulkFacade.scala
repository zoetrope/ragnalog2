package com.arielnetworks.ragnalog.port.adapter.embulk

import java.io.File
import java.nio.file.Path

import scala.sys.process.{Process, ProcessLogger}
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
  def run(yaml: Path): Try[String] = {
    try {
      val stdout = new StringBuilder
      val stderr = new StringBuilder

      val ret = Process(s"$embulk run $yaml -b $bundleDir") ! ProcessLogger(s=>stdout.append(s + "\n"), s => stderr.append(s + "\n"))

      println(s"ret: $ret")
      println("****************************")
      println(s"stdout: ${stdout.toString}")
      println("****************************")
      println(s"stderr: ${stderr.toString}")
      println("****************************")

      Success(stdout.toString)
    } catch {
      case e: Throwable => Failure(e)
    }
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

  def create(): EmbulkFacade = new EmbulkFacade(embulkConfiguration)
}
