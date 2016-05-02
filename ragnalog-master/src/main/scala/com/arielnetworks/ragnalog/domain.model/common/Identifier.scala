package com.arielnetworks.ragnalog.domain.model.common

trait Identifier[+I] {
  def value: I
}

case class EmptyId(value: String = "") extends Identifier[String]
