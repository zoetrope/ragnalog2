package com.arielnetworks.ragnalog.domain.model.detector

trait LogTypeDetector {
  def detect(path: String): Option[String]
}
