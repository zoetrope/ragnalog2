package com.arielnetworks.ragnalog.application

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import com.arielnetworks.ragnalog.application.archive.ArchiveService
import com.arielnetworks.ragnalog.application.container.ContainerService
import com.arielnetworks.ragnalog.application.logfile.LogFileService
import com.arielnetworks.ragnalog.port.adapter.persistence.repository.{ArchiveRepositoryOnElasticsearch, ContainerRepositoryOnElasticsearch, LogFileRepositoryOnElasticsearch}
import com.arielnetworks.ragnalog.port.adapter.service.{KibanaAdapter, RegistrationDispatcher}
import com.arielnetworks.ragnalog.port.adapter.specification.ElasticsearchIdPatternSpecification
import com.sksamuel.elastic4s.{ElasticClient, ElasticsearchClientUri}
import org.elasticsearch.common.settings.Settings

//TODO: add interface
object ServiceRegistry {

  val elasticsearchSettings = Settings.settingsBuilder().put("cluster.name", "ragnalog2.elasticsearch")
  implicit val elasticClient = ElasticClient.transport(elasticsearchSettings.build, ElasticsearchClientUri("elasticsearch://localhost:9300"))

  implicit val actorSystem = ActorSystem("ragnalog-master")
  val materializer = ActorMaterializer()

  val idSpec = new ElasticsearchIdPatternSpecification

  val containerRepository = new ContainerRepositoryOnElasticsearch(elasticClient)
  val archiveRepository = new ArchiveRepositoryOnElasticsearch(elasticClient)
  val logFileRepository = new LogFileRepositoryOnElasticsearch(elasticClient)

  val visualizationAdapter = new KibanaAdapter
  val registrationAdapter = new RegistrationDispatcher

  val logFileService = new LogFileService(logFileRepository, registrationAdapter, visualizationAdapter)
  val archiveService = new ArchiveService(archiveRepository, logFileService)
  val containerService = new ContainerService(containerRepository, archiveService, idSpec)

}
