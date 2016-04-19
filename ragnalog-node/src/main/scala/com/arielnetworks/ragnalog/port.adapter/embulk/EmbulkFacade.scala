package com.arielnetworks.ragnalog.port.adapter.embulk

import java.io.File

import com.arielnetworks.ragnalog.application.RegistrationResult
import org.embulk.EmbulkEmbed

class EmbulkFacade(embulkEmbed: EmbulkEmbed, yaml: String) {

  val loader = embulkEmbed.newConfigLoader()
  val config = loader.fromYamlString(yaml)

  def guess() = {
    val diff = embulkEmbed.guess(config)
    config.merge(diff)
  }

  //TODO: Should this class have RegistrationResponse?
  def run(): RegistrationResult = {

    val result = embulkEmbed.run(config)
    val errorCount = result.getIgnoredExceptions.size()
    val res =
      if (result.getIgnoredExceptions.isEmpty) {
        new RegistrationResult("", errorCount)
      } else {
        //TODO: This message should be assemble by Application layer.
        new RegistrationResult(result.getIgnoredExceptions.size + " errors. first message: " + result.getIgnoredExceptions.get(0).getMessage, errorCount)
      }

    return res
  }
}

class EmbulkFacadeFactory(embulkConfiguration: EmbulkConfiguration) {
  val embulkEmbed = EmbulkEmbedFactory.create(embulkConfiguration).get //TODO:

  def create(configPath: String): EmbulkFacade = new EmbulkFacade(embulkEmbed, configPath)
}
