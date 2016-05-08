package com.arielnetworks.ragnalog.domain.model.common

trait Entity[ID <: Identifier[_, _]] {

  val id: ID

}
