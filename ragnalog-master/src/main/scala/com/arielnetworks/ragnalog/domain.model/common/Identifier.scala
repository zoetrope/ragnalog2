package com.arielnetworks.ragnalog.domain.model.common

trait Identifier[+I, +P] {
  def id: I
  def parent: P
}

case class EmptyId(id: String = "", parent: String = "") extends Identifier[String, String]
