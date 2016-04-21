package com.arielnetworks.ragnalog.port.adapter.embulk

import scalax.file.Path

import scala.sys.process.{Process, ProcessLogger}
import scala.language.postfixOps
import com.arielnetworks.ragnalog.application.RegistrationResult

import scala.util.{Failure, Success, Try}

case class CommandResult(command: String, returnCode: Int, stdout: String, stderr: String)

case class PluginInfo(name: String, versions: Seq[String])

case class ProcessRuntimeException(message: String) extends Exception(message)

class EmbulkFacade(config: EmbulkConfiguration) {

  val embulk = config.embulkPath
  val bundleDir = config.bundleDirectory


  def guess() = {
  }

  //TODO: Should this class have RegistrationResponse?
  def run(yaml: Path): Try[String] = {
    executeProcess(s"$embulk run $yaml -b $bundleDir")
      .map(res => res.returnCode match {
        case 0 => res.stdout
        case _ => throw new ProcessRuntimeException(res.stdout)
      })
  }

  def plugins(): Try[Seq[PluginInfo]] = {
    val pluginNamePattern = """^(embulk-\S+) \((.*)\)$""".r

    executeProcess(s"$embulk gem list -b $bundleDir")
      .map(res => res.returnCode match {
        case 0 =>
          res.stdout.split(System.lineSeparator()).collect({
            case pluginNamePattern(name, versions) => PluginInfo(name, versions.split(",").map(_.trim).toList)
          })
        case _ => throw new ProcessRuntimeException(res.stdout)
      })
  }

  private val bufferSize = 65535

  private def executeProcess(command: String): Try[CommandResult] = {
    try {
      val stdout = new StringBuilder
      val stderr = new StringBuilder

      val ret = Process(command) ! ProcessLogger(
        s => {
          if (stdout.length + s.length < bufferSize) stdout.append(s + System.lineSeparator())
          //TODO: logging
        },
        s => {
          if (stderr.length + s.length < bufferSize) stderr.append(s + System.lineSeparator())
          //TODO: logging
        })

      Success(CommandResult(command, ret, stdout.toString(), stderr.toString()))
    } catch {
      case e: Throwable => Failure(e)
    }
  }
}

class EmbulkFacadeFactory(embulkConfiguration: EmbulkConfiguration) {

  def create(): EmbulkFacade = new EmbulkFacade(embulkConfiguration)
}
