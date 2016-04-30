package com.arielnetworks.ragnalog.port.adapter.embulk

import java.io.ByteArrayOutputStream
import java.util.zip.{ZipEntry, ZipOutputStream}

import com.arielnetworks.ragnalog.support.LoanSupport

import scala.language.postfixOps
import scala.sys.process.{Process, ProcessLogger}
import scala.util.{Failure, Success, Try}
import scalax.file.Path

case class CommandResult(command: String, returnCode: Int, stdout: Array[Byte], stderr: Array[Byte])

case class CommandFailureException(message: String, log: Array[Byte]) extends Exception(message)

class EmbulkFacade(config: EmbulkConfiguration) extends LoanSupport {

  val embulk = config.embulkPath.path
  val bundleDir = config.bundleDirectory.path

  def run(yaml: Path): Try[Array[Byte]] = {
    val command = s"$embulk run ${yaml.path} -b $bundleDir"
    executeProcess(command)
      .map(res => res.returnCode match {
        case 0 => res.stdout
        case _ => throw new CommandFailureException(s"failed to command: $command", res.stdout)
      })
  }

  def guess() = {
  }

  private def executeProcess(command: String): Try[CommandResult] = {
    try {

      val stdoutByteArrayStream = new ByteArrayOutputStream()
      val stderrByteArrayStream = new ByteArrayOutputStream()

      val returnCode = using(new ZipOutputStream(stdoutByteArrayStream)) { stdoutZipStream =>
        using(new ZipOutputStream(stderrByteArrayStream)) { stderrZipStream =>

          stdoutZipStream.putNextEntry(new ZipEntry("embulk_stdout.log"))
          stderrZipStream.putNextEntry(new ZipEntry("embulk_stderr.log"))

          Process(command) ! ProcessLogger(
            s => {
              stdoutZipStream.write((s + System.lineSeparator()).getBytes)
              //TODO: logging
            },
            s => {
              stderrZipStream.write((s + System.lineSeparator()).getBytes)
              //TODO: logging
            })
        }
      }

      Success(CommandResult(command, returnCode, stdoutByteArrayStream.toByteArray, stderrByteArrayStream.toByteArray))
    } catch {
      case e: Throwable => Failure(e)
    }
  }
}

class EmbulkFacadeFactory(embulkConfiguration: EmbulkConfiguration) {
  def create(): EmbulkFacade = new EmbulkFacade(embulkConfiguration)
}
