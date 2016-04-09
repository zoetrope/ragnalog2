package com.arielnetworks.ragnalog.domain.model.common

trait Entity[ID <: Identifier[_]] {

  val id: ID

}
