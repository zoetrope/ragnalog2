package com.arielnetworks.ragnalog.port.adapter.embulk

import java.io.File

import com.arielnetworks.ragnalog.application.RegistrationResponse
import org.embulk.EmbulkEmbed

class EmbulkFacade(embulkEmbed:EmbulkEmbed, configPath:String) {

  val loader = embulkEmbed.newConfigLoader()
  val config = loader.fromYamlFile(new File(configPath))

  def guess() = {
    val diff = embulkEmbed.guess(config)
    config.merge(diff)
  }

  //TODO: Should this class have RegistrationResponse?
  def run(): RegistrationResponse = {

    val result = embulkEmbed.run(config)
    val errorCount = result.getIgnoredExceptions.size()
    val res =
      if (result.getIgnoredExceptions.isEmpty) {
        new RegistrationResponse("", errorCount)
      } else {
        //TODO: This message should be assemble by Application layer.
        new RegistrationResponse(result.getIgnoredExceptions.size + " errors. first message: " + result.getIgnoredExceptions.get(0).getMessage, errorCount)
      }

    return res
  }
}

class EmbulkFacadeFactory(embulkConfiguration: EmbulkConfiguration) {
  val embulkEmbed = EmbulkEmbedFactory.create(embulkConfiguration).get //TODO:

  def create(configPath:String): EmbulkFacade = new EmbulkFacade(embulkEmbed, configPath)
}
