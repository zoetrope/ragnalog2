package com.arielnetworks.ragnalog.test

import org.scalatest.concurrent.ScalaFutures
import org.scalatest.{DiagrammedAssertions, FunSpec}

trait Trans {
  protected def convert(input: String): String
}

trait TransImpl extends Trans {
  protected def convert(input: String): String = {
    input.toLowerCase
  }
}

abstract class Base extends Trans {
  def func(input: String): String = {
    convert(input + "hoge")
  }
}

class Impl extends Base with TransImpl {

}

class MixinTest extends FunSpec with DiagrammedAssertions with ScalaFutures {

  describe("Test should work") {
    val impl = new Impl
    assert(impl.func("FUGA") == "fugahoge")

  }
}
