package com.arielnetworks.ragnalog.application

import com.arielnetworks.ragnalog.domain.model.container.ContainerId
import org.scalatest.{BeforeAndAfter, DiagrammedAssertions, FunSpec, FunSuite}
import org.scalatest.concurrent.ScalaFutures

class ContainerServiceSpec extends FunSpec with DiagrammedAssertions with ScalaFutures {

  val containerService = new ContainerService

  describe("create a container") {
    describe("with all valid parameters") {
      it("should be created a container") {
        val future = containerService.createContainer(Some("test_id"), Some("test-name"), Some("test-description"))
        whenReady(future) {
          case Right(x) =>
            assert(x.id == ContainerId("test_id"))
            assert(x.name == "test-name")
            assert(x.description.contains("test-description"))
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
          case Left(x) => fail(x.toString)
        }
      }
    }

    describe("without name") {
      it("should be created a container that name is the same as id") {

      }

    }

    describe("without id and name") {
      it("should fail to create a container") {

      }
    }
    describe("without id and invalid name") {
      it("should fail to create a container") {

      }
    }

    describe("without description") {
      it("should be created a container") {

      }
    }

    describe("with invalid id") {
      it("should fail to create a container") {

      }
    }

    describe("already exists container") {
      it("should fail to create a container") {

      }
    }
  }
}

