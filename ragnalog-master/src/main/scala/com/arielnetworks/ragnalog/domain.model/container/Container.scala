package com.arielnetworks.ragnalog.domain.model.container

import com.arielnetworks.ragnalog.domain.model.common.{Entity, Identifier}

case class ContainerId(value: String) extends Identifier[String]

case class Container(id: ContainerId, name: String, description: Option[String], isActive: Boolean) extends Entity[ContainerId] {

  def activate() = ???

  def deactivate() = ???

}
