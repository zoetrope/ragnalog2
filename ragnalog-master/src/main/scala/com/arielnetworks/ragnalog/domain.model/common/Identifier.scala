package com.arielnetworks.ragnalog.domain.model.common

trait Identifier[+I] {
  def value: I
}
