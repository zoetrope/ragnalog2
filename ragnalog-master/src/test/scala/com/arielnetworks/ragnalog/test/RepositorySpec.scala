package com.arielnetworks.ragnalog.test

import com.arielnetworks.ragnalog.domain.model.container.{Container, ContainerId}
import com.arielnetworks.ragnalog.port.adapter.persistence.repository.ContainerRepositoryOnElasticsearch
import com.sksamuel.elastic4s.{ElasticClient, ElasticsearchClientUri}
import org.elasticsearch.common.settings.Settings
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.{DiagrammedAssertions, FunSpec}

class RepositorySpec extends FunSpec with DiagrammedAssertions with ScalaFutures {

  val settings = Settings.settingsBuilder().put("cluster.name", "ragnalog.elasticsearch")
  val client = ElasticClient.transport(settings.build,ElasticsearchClientUri("elasticsearch://localhost:9300"))
  val containerRepository = new ContainerRepositoryOnElasticsearch(client)

  describe("Test should work") {
    val container = new Container(ContainerId("test_id123"),"test-name", Some("test-description"), true)
    val f = containerRepository.save(container)
    whenReady(f.failed) { result =>
      assert(true)
    }
  }

  describe("delete") {
    val f = containerRepository.deleteById(ContainerId("test_id123"))
    whenReady(f.failed) { result =>
      assert(true)
    }
  }

  // NoNodesAvailableException
  // recover node

}
