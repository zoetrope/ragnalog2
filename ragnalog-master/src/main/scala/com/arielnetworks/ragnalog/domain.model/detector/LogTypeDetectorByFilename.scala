package com.arielnetworks.ragnalog.domain.model.detector

class LogTypeDetectorByFilename(patterns: Seq[LogTypePattern]) extends LogTypeDetector {

  override def detect(path: String): Option[String] = {

    val pos = path.lastIndexOf("/")
    val fileName = path.substring(pos + 1)

    patterns
      .filter(p => p.pattern.matcher(fileName).matches())
      .sortWith((x1, x2) => x1.priority > x2.priority)
      .headOption
      .map(_.typeName)

  }
}
