package com.arielnetworks.ragnalog.port.adapter.embulk

import java.io.File
import java.nio.file.{Files, Path, Paths}

import com.arielnetworks.ragnalog.test.EmbulkTestSupport
import org.scalatest.{BeforeAndAfterAll, DiagrammedAssertions, FunSpec}

class EmbulkEmbedFactorySpec extends FunSpec with DiagrammedAssertions with BeforeAndAfterAll with EmbulkTestSupport {

  private def removeAll(path: Path) = {
    def recursive(f: File): Seq[File] = f.listFiles.filter(_.isDirectory).flatMap(recursive) ++ f.listFiles
    recursive(path.toFile).foreach { f => f.delete() }
  }

  override def beforeAll(): Unit = {
    Files.createDirectories(embulkWorkingDir)
    Files.createDirectories(embulkLogDir)
  }

  override def afterAll(): Unit = {
    removeAll(embulkWorkingDir)
    removeAll(embulkLogDir)
  }

  describe("create EmbulkEmbed") {
    describe("valid config") {
      it("should be created EmbulkEmbed") {
        val config = EmbulkConfiguration(
          embulkPluginDir,
          embulkWorkingDir,
          embulkLogFilePath,
          Map(
            "grok" -> grokPluginConfig,
            "elasticsearch" -> elasticsearchPluginConfig
          ),
          Map("apache.access" -> apacheAccessConfig)
        )

        val embed = EmbulkEmbedFactory.create(config)
        if (embed.isFailure) embed.failed.get.printStackTrace()
        assert(embed.isSuccess)
      }
    }

    describe("invalid plugin directory") {
      it("should not be created EmbulkEmbed") {
        val config = EmbulkConfiguration(
          Paths.get(userDir,"invalid_plugins_directory"),
          embulkWorkingDir,
          embulkLogFilePath,
          Map(
            "grok" -> grokPluginConfig,
            "elasticsearch" -> elasticsearchPluginConfig
          ),
          Map("apache.access" -> apacheAccessConfig)
        )

        val embed = EmbulkEmbedFactory.create(config)
        assert(embed.isFailure)
      }
    }

    describe("invalid plugin name") {
      it("should not be created EmbulkEmbed") {
      }
    }

    describe("invalid plugin type") {
      it("should not be created EmbulkEmbed") {
      }
    }

    describe("invalid working directory") {
      it("should not be created EmbulkEmbed") {

      }
    }

    describe("permission denied working directory") {
      it("should not be created EmbulkEmbed") {

      }
    }

    describe("invalid log directory") {
      it("should not be created EmbulkEmbed") {

      }
    }
  }
}
