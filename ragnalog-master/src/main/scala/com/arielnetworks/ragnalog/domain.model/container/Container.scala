package com.arielnetworks.ragnalog.domain.model.container

import com.arielnetworks.ragnalog.domain.model.common.{Entity, Identifier}

import scala.concurrent.Future

object ContainerStatus extends Enumeration {
  val Active, Inactive = Value
}


case class ContainerId(id: String, parent: String = null) extends Identifier[String, String]

case class Container
(
  id: ContainerId,
  name: String,
  description: Option[String],
  status: ContainerStatus.Value
)
  extends Entity[ContainerId] {

  def change(name: String, description: Option[String]): Container = copy(name = name, description = description)

  def activate(): Container = copy(status = ContainerStatus.Active)

  // containerRepository, indexPattern, index.close

  def deactivate(): Container = copy(status = ContainerStatus.Inactive)

}
