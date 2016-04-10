package com.arielnetworks.ragnalog.application

import com.arielnetworks.ragnalog.domain.model.container.{ContainerId, ContainerStatus}
import com.arielnetworks.ragnalog.port.adapter.persistence.repository.ContainerRepositoryOnElasticsearch
import com.arielnetworks.ragnalog.port.adapter.specification.ElasticsearchIdPatternSpecification
import com.sksamuel.elastic4s.{ElasticClient, ElasticsearchClientUri}
import org.elasticsearch.common.settings.Settings
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.time.{Seconds, Span}
import org.scalatest.{DiagrammedAssertions, FunSpec}

class ContainerServiceSpec extends FunSpec with DiagrammedAssertions with ScalaFutures {

  val idSpec = new ElasticsearchIdPatternSpecification
  val settings = Settings.settingsBuilder().put("cluster.name", "ragnalog.elasticsearch")
  val client = ElasticClient.transport(settings.build, ElasticsearchClientUri("elasticsearch://localhost:9300"))
  val containerRepository = new ContainerRepositoryOnElasticsearch(client)
  val containerService = new ContainerService(idSpec, containerRepository)

  describe("create a container") {
    describe("with all valid parameters") {
      it("should be created a container") {
        val future = containerService.createContainer(Some("test_id_1"), Some("test-name"), Some("test-description"))
        whenReady(future, timeout(Span(1, Seconds))) {
          container =>
            assert(container.id == ContainerId("test_id_1"))
            assert(container.name == "test-name")
            assert(container.description.contains("test-description"))
            assert(container.status == ContainerStatus.Active)
        }
      }
    }

    describe("without id") {
      it("should be created a container that id is UUID") {
        val future = containerService.createContainer(None, Some("test-name"), Some("test-description"))
        whenReady(future, timeout(Span(1, Seconds))) {
          container =>
            assert(container.id.value.matches("^[a-z0-9]{32}$"))
            assert(container.name == "test-name")
            assert(container.description.contains("test-description"))
            assert(container.status == ContainerStatus.Active)
        }
      }
    }

    describe("without name") {
      it("should be created a container that name is the same as id") {
        val future = containerService.createContainer(Some("test_id_2"), None, Some("test-description"))
        whenReady(future, timeout(Span(1, Seconds))) {
          container =>
            assert(container.id == ContainerId("test_id_2"))
            assert(container.name == "test_id_2")
            assert(container.description.contains("test-description"))
            assert(container.status == ContainerStatus.Active)
        }
      }
    }

    describe("without description") {
      it("should be created a container") {
        val future = containerService.createContainer(Some("test_id_3"), Some("test-name"), None)
        whenReady(future, timeout(Span(1, Seconds))) {
          container =>
            assert(container.id == ContainerId("test_id_3"))
            assert(container.name == "test-name")
            assert(container.description.isEmpty)
            assert(container.status == ContainerStatus.Active)
        }
      }
    }

    describe("without id and name") {
      it("should fail to create a container") {
        val future = containerService.createContainer(None, None, Some("test-description"))
        whenReady(future.failed, timeout(Span(1, Seconds))) {
          case x: IllegalArgumentException => //OK
        }
      }
    }

    describe("with invalid id") {
      it("should fail to create a container") {
        val future = containerService.createContainer(Some("テスト"), Some("test-name"), Some("test-description"))
        whenReady(future.failed, timeout(Span(1, Seconds))) {
          case x: IllegalArgumentException => //OK
        }
      }
    }

    describe("already exists container") {
      it("should fail to create a container") {

      }
    }
  }
}

