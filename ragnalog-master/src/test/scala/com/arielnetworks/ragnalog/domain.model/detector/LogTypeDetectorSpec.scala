package com.arielnetworks.ragnalog.domain.model.detector

import java.util.regex.Pattern

import org.scalatest.{DiagrammedAssertions, FunSpec}

class LogTypeDetectorSpec extends FunSpec with DiagrammedAssertions {

  val detector = new LogTypeDetectorByFilename(
    List(
      new LogTypePattern(30, Pattern.compile(".*access.*", Pattern.CASE_INSENSITIVE), "apache.access")
    )
  )
  describe("detect") {
    val logType = detector.detect("/tmp/apache.access.log")
    assert(logType == Some("apache.access"))
  }
}
