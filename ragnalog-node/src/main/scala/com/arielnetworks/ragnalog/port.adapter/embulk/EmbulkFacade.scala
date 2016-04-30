package com.arielnetworks.ragnalog.port.adapter.embulk

import java.io.ByteArrayOutputStream
import java.util.zip.{ZipEntry, ZipOutputStream}

import scala.language.postfixOps
import scala.sys.process.{Process, ProcessLogger}
import scala.util.{Failure, Success, Try}
import scalax.file.Path

case class CommandResult(command: String, returnCode: Int, stdout: Array[Byte], stderr: Array[Byte])

case class CommandFailureException(message: String, log: Array[Byte]) extends Exception(message)

class EmbulkFacade(config: EmbulkConfiguration) {

  val embulk = config.embulkPath.path
  val bundleDir = config.bundleDirectory.path


  def run(yaml: Path): Try[Array[Byte]] = {
    executeProcess(s"$embulk run ${yaml.path} -b $bundleDir")
      .map(res => res.returnCode match {
        case 0 => res.stdout
        case _ => throw new CommandFailureException("failed to command: embulk run", res.stdout)
      })
  }

  def guess() = {
  }

  private def executeProcess(command: String): Try[CommandResult] = {
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

      Success(CommandResult(command, ret, stdoutByteArrayStream.toByteArray, stderrByteArrayStream.toByteArray))
    } catch {
      case e: Throwable => Failure(e)
    }
  }
}

class EmbulkFacadeFactory(embulkConfiguration: EmbulkConfiguration) {

  def create(): EmbulkFacade = new EmbulkFacade(embulkConfiguration)
}
