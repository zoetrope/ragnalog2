package com.arielnetworks.ragnalog.test

import com.arielnetworks.ragnalog.support.FutureSupport
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.{DiagrammedAssertions, FunSpec}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success}

class Sandbox extends FunSpec with DiagrammedAssertions with ScalaFutures with FutureSupport {

  describe("test for FutureSupport") {
    val f1 = Future.successful(1)
    val f2 = Future.successful(2)
    val f3 = Future.failed(new UnsupportedOperationException("f3"))
    val f4 = Future.failed(new UnsupportedOperationException("f4"))
    val f5 = Future.successful(5)
    val fs = Seq(f1, f2, f3, f4, f5)

    val r = sequenceTryAll(fs: _*)
    whenReady(r) {
      ns =>
        println(ns.length)
        println(ns)
    }
  }

}
