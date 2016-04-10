package com.arielnetworks.ragnalog.application

import com.arielnetworks.ragnalog.domain.model.container.ContainerId
import com.arielnetworks.ragnalog.port.adapter.persistence.repository.ContainerRepositoryOnElasticsearch
import com.arielnetworks.ragnalog.port.adapter.specification.ElasticsearchIdPatternSpecification
import com.sksamuel.elastic4s.{ElasticClient, ElasticsearchClientUri}
import org.elasticsearch.common.settings.Settings
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.{DiagrammedAssertions, FunSpec}

class ContainerServiceSpec extends FunSpec with DiagrammedAssertions with ScalaFutures {

  val idSpec = new ElasticsearchIdPatternSpecification
  val settings = Settings.settingsBuilder().put("cluster.name", "ragnalog.elasticsearch")
  val client = ElasticClient.transport(settings.build,ElasticsearchClientUri("elasticsearch://localhost:9300"))
  val containerRepository = new ContainerRepositoryOnElasticsearch(client)
  val containerService = new ContainerService(idSpec, containerRepository)

  describe("create a container") {
    describe("with all valid parameters") {
      it("should be created a container") {
        val future = containerService.createContainer(Some("test_id"), Some("test-name"), Some("test-description"))
        whenReady(future) {
          case Right(x) =>
            assert(x.id == ContainerId("test_id"))
            assert(x.name == "test-name")
            assert(x.description.contains("test-description"))
            assert(x.isActive == true)
          case Left(x) => fail(x.toString)
        }
      }
    }

    describe("without id") {
      it("should be created a container that id is UUID") {
        val future = containerService.createContainer(None, Some("test-name"), Some("test-description"))
        whenReady(future) {
          case Right(x) =>
            assert(x.id.value.matches("^[a-z0-9]{32}$"))
            assert(x.name == "test-name")
            assert(x.description.contains("test-description"))
            assert(x.isActive == true)
          case Left(x) => fail(x.toString)
        }
      }
    }

    describe("without name") {
      it("should be created a container that name is the same as id") {
        val future = containerService.createContainer(Some("test_id"), None, Some("test-description"))
        whenReady(future) {
          case Right(x) =>
            assert(x.id == ContainerId("test_id"))
            assert(x.name == "test_id")
            assert(x.description.contains("test-description"))
            assert(x.isActive == true)
          case Left(x) => fail(x.toString)
        }
      }
    }

    describe("without description") {
      it("should be created a container") {
        val future = containerService.createContainer(Some("test_id"), Some("test-name"), None)
        whenReady(future) {
          case Right(x) =>
            assert(x.id == ContainerId("test_id"))
            assert(x.name == "test-name")
            assert(x.description.isEmpty)
            assert(x.isActive == true)
          case Left(x) => fail(x.toString)
        }
      }
    }

    describe("without id and name") {
      it("should fail to create a container") {
        val future = containerService.createContainer(None, None, Some("test-description"))
        whenReady(future) {
          case Right(x) => fail()
          case Left(InvalidArgument(x)) => // OK
          case _ => fail()
        }
      }
    }

    describe("with invalid id") {
      it("should fail to create a container") {
        val future = containerService.createContainer(Some("テスト"), Some("test-name"), Some("test-description"))
        whenReady(future) {
          case Right(x) => fail()
          case Left(InvalidId(x)) => // OK
          case _ => fail()
        }
      }
    }

    describe("already exists container") {
      it("should fail to create a container") {

      }
    }
  }
}

