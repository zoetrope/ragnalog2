package com.arielnetworks.ragnalog.port.adapter

import com.arielnetworks.ragnalog.domain.model.container.{Container, ContainerId, ContainerStatus}
import com.arielnetworks.ragnalog.port.adapter.persistence.repository.ContainerRepositoryOnElasticsearch
import com.arielnetworks.ragnalog.test.ElasticsearchTestSupport
import com.sksamuel.elastic4s.{ElasticClient, ElasticsearchClientUri}
import org.elasticsearch.common.settings.Settings
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.{DiagrammedAssertions, FunSpec}

import scala.concurrent.ExecutionContext.Implicits.global

class RepositorySpec extends FunSpec with DiagrammedAssertions with ScalaFutures with ElasticsearchTestSupport {

  val containerRepository = new ContainerRepositoryOnElasticsearch(elasticClient)

  describe("create") {
    val container = new Container(ContainerId("test_id"), "test-name", Some("test-description"), ContainerStatus.Active)
    val f = containerRepository.add(container)
    whenReady(f.failed) { result =>
      assert(true)
    }
  }

  describe("get") {

    //    var f1 = client.execute(
    //      index into ".ragnalog2" / "container" id "invalid_id" fields("description"->"hoge")
    //    )

    val f2 = containerRepository.resolveById(ContainerId("invalid_id"))
    whenReady(f2) { result =>
      println("*************" + result)
      assert(result.name == "test-name")
    }
  }

  describe("Test should work") {
    val container = new Container(ContainerId("test_id123"), "test-name", Some("test-description"), ContainerStatus.Active)
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

  describe("list") {
    val f = for {
      count <- containerRepository.countByStatus(ContainerStatus.Active)
      containers <- containerRepository.searchByStatus(0, count.asInstanceOf[Int], ContainerStatus.Active)
    } yield containers

    whenReady(f) { result =>
      println(result)
    }
  }

  // NoNodesAvailableException
  // recover node

}
