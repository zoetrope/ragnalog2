package com.arielnetworks.ragnalog.application

import com.arielnetworks.ragnalog.domain.model.container.ContainerId
import org.scalatest.{DiagrammedAssertions, FunSuite}
import org.scalatest.concurrent.ScalaFutures

class ContainerServiceSpec extends FunSuite with DiagrammedAssertions with ScalaFutures {


  test("create container") {
    val containerService = new ContainerService

    val future = containerService.createContainer(Some("test_id"), Some("test-name"), Some("test-description"))

    whenReady(future) {
      case Right(x) =>
        assert(x.id == ContainerId("test_id"))
        assert(x.name == "test-name")
        assert(x.description.contains("test-description"))
      case Left(x) => fail(x.toString)
    }
  }

//  test("create container without id") = ???
//  test("create container without name") = ???
//  test("create container with invalid id") = ???
//  test("create container without id and name") = ???
}
