package com.arielnetworks.ragnalog.port.adapter.embulk

import java.io.{ByteArrayInputStream, ByteArrayOutputStream}
import java.util
import java.util.Scanner
import java.util.zip.{ZipEntry, ZipInputStream, ZipOutputStream}

import scalax.file.Path
import scala.sys.process.{Process, ProcessLogger}
import scala.language.postfixOps
import com.arielnetworks.ragnalog.application.RegistrationResult

import scala.util.{Failure, Success, Try}

case class CommandResult(command: String, returnCode: Int, stdout: String, stderr: String)

case class CommandResultZip(command: String, returnCode: Int, stdout: Array[Byte], stderr: Array[Byte])

case class PluginInfo(name: String, versions: Seq[String])

case class ProcessRuntimeException(message: String) extends Exception(message)

class EmbulkFacade(config: EmbulkConfiguration) {

  val embulk = config.embulkPath.path
  val bundleDir = config.bundleDirectory.path


  def guess() = {
  }

  private def decode(byteArray: Array[Byte]): String = {
    val zis = new ZipInputStream(new ByteArrayInputStream(byteArray))

    val output = new ByteArrayOutputStream()
    Iterator.continually(zis.getNextEntry)
      .takeWhile(_ != null).filterNot(_.isDirectory)
      .foreach(entry => {
        Iterator.continually(zis.read()).takeWhile(_ != -1).foreach(b => {
          output.write(b)
        })
      })
    zis.close()
    output.close()

    output.toString()
  }

  //TODO: Should this class have RegistrationResponse?
  def run(yaml: Path): Try[String] = {
    executeProcessToZip(s"$embulk run ${yaml.path} -b $bundleDir")
      .map(res => res.returnCode match {
        case 0 => decode(res.stdout)
        case _ => throw new ProcessRuntimeException(decode(res.stdout))
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

  private def executeProcessToZip(command: String): Try[CommandResultZip] = {
    try {

      val stdoutByteArrayStream = new ByteArrayOutputStream()
      val stdoutZipStream = new ZipOutputStream(stdoutByteArrayStream)

      val stderrByteArrayStream = new ByteArrayOutputStream()
      val stderrZipStream = new ZipOutputStream(stderrByteArrayStream)

      stdoutZipStream.putNextEntry(new ZipEntry("embulk_stdout.log"))
      stderrZipStream.putNextEntry(new ZipEntry("embulk_stderr.log"))

      val ret = Process(command) ! ProcessLogger(
        s => {
          stdoutZipStream.write((s + "\n").getBytes)
          //TODO: logging
        },
        s => {
          stderrZipStream.write((s + "\n").getBytes)
          //TODO: logging
        })

      stdoutZipStream.close()
      stderrZipStream.close()

      Success(CommandResultZip(command, ret, stdoutByteArrayStream.toByteArray, stderrByteArrayStream.toByteArray))
    } catch {
      case e: Throwable => Failure(e)
    }
  }
}

class EmbulkFacadeFactory(embulkConfiguration: EmbulkConfiguration) {

  def create(): EmbulkFacade = new EmbulkFacade(embulkConfiguration)
}
