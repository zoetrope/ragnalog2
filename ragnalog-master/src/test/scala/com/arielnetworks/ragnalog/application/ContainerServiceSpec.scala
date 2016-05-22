package com.arielnetworks.ragnalog.application

import akka.actor.Props
import com.arielnetworks.ragnalog.application.archive.ArchiveService
import com.arielnetworks.ragnalog.application.container.ContainerService
import com.arielnetworks.ragnalog.application.container.data.{AddContainerRequest, ContainerResponse}
import com.arielnetworks.ragnalog.application.logfile.LogFileService
import com.arielnetworks.ragnalog.domain.model.container.ContainerId
import com.arielnetworks.ragnalog.port.adapter.persistence.repository.{ArchiveRepositoryOnElasticsearch, ContainerRepositoryOnElasticsearch, LogFileRepositoryOnElasticsearch}
import com.arielnetworks.ragnalog.port.adapter.service.{AdministratorOnElasticsearch, DispatcherActor, KibanaAdapter, RegistrationDispatcher}
import com.arielnetworks.ragnalog.port.adapter.specification.ElasticsearchIdPatternSpecification
import com.arielnetworks.ragnalog.test.ElasticsearchTestSupport
import com.typesafe.config.ConfigFactory
import org.elasticsearch.index.engine.DocumentAlreadyExistsException
import org.elasticsearch.transport.RemoteTransportException
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.{BeforeAndAfterAll, DiagrammedAssertions, FunSpec}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Await, Future}
import scala.io.Source

class ContainerServiceSpec
  extends FunSpec with DiagrammedAssertions with ScalaFutures with BeforeAndAfterAll
    with ElasticsearchTestSupport {

  val indexName = ".ragnalog2_test"
  val idSpec = new ElasticsearchIdPatternSpecification
  val containerRepository = new ContainerRepositoryOnElasticsearch(elasticClient, indexName)
  val visualizationAdapter = new KibanaAdapter
  val registrationAdapter = new RegistrationDispatcher(null)
  val archiveRepository = new ArchiveRepositoryOnElasticsearch(elasticClient, indexName)
  val logFileRepository = new LogFileRepositoryOnElasticsearch(elasticClient, indexName)
  val logFileService = new LogFileService(logFileRepository, archiveRepository, registrationAdapter, visualizationAdapter)
  val administrator = new AdministratorOnElasticsearch(elasticClient)
  val archiveService = new ArchiveService(archiveRepository, logFileService)
  val containerService = new ContainerService(containerRepository, archiveService, idSpec)

  val mapping = Source.fromURL(administrator.getClass.getClassLoader.getResource("elasticsearch/mappings.json"))
    .getLines()
    .mkString(System.lineSeparator())

  override def beforeAll(): Unit = {
    val f = for {
      existIndex <- administrator.existsIndex(indexName)
      _ <- if (existIndex) {
        administrator.deleteIndex(indexName)
      } else {
        Future.successful(())
      }
      _ <- administrator.createMapping(indexName, mapping)
    } yield ()

    Await.result(f, testTimeout)
  }

  override def afterAll(): Unit = {
    val f = for {
      existIndex <- administrator.existsIndex(indexName)
      _ <- if (existIndex) {
        administrator.deleteIndex(indexName)
      } else {
        Future.successful(())
      }
    } yield ()

    Await.result(f, testTimeout)
  }

  describe("create a container") {
    describe("with all valid parameters") {
      it("should create a container") {
        val future = containerService.createContainer(new AddContainerRequest("test_id_1", Some("test-name"), Some("test-description")))
        whenReady(future, timeout(testTimeout)) {
          container =>
            assert(container === ContainerResponse("test_id_1", "test-name", Some("test-description"), "Active"))
        }
      }
    }

    describe("without name") {
      it("should create a container that has the same name as id") {
        val future = containerService.createContainer(new AddContainerRequest("test_id_2", None, Some("test-description")))
        whenReady(future, timeout(testTimeout)) {
          container =>
            assert(container === ContainerResponse("test_id_2", "test_id_2", Some("test-description"), "Active"))
        }
      }
    }

    describe("without description") {
      it("should create a container") {
        val future = containerService.createContainer(new AddContainerRequest("test_id_3", Some("test-name"), None))
        whenReady(future, timeout(testTimeout)) {
          container =>
            assert(container === ContainerResponse("test_id_3", "test-name", None, "Active"))
        }
      }
    }

    describe("with invalid id") {
      it("should fail to create a container") {
        val future = containerService.createContainer(new AddContainerRequest("テスト", Some("test-name"), None))
        whenReady(future.failed, timeout(testTimeout)) {
          case x: IllegalArgumentException => //OK
        }
      }
    }

    describe("already exists container") {
      it("should fail to create a container") {
        val future = for {
          _ <- containerService.createContainer(new AddContainerRequest("test_id_4", Some("test-name"), Some("test-description")))
          _ <- containerService.createContainer(new AddContainerRequest("test_id_4", Some("test-name"), Some("test-description")))
        } yield ()
        whenReady(future.failed, timeout(testTimeout)) {
          case x: RemoteTransportException =>
            x.getCause.getCause match {
              case e: DocumentAlreadyExistsException => // OK
            }
        }
      }
    }
  }

  describe("remove container") {
    describe("create and remove") {
      it("should fail to create a container") {
        val future = for {
          _ <- containerService.createContainer(new AddContainerRequest("test_id_5", Some("test-name"), Some("test-description")))
          _ <- containerService.removeContainer(ContainerId("test_id_5"))
        } yield ()

        whenReady(future, timeout(testTimeout)) {
          _ => assert(true)
        }
      }
    }
  }
}

