package com.arielnetworks.ragnalog.domain.model.container

import com.arielnetworks.ragnalog.domain.model.common.{Entity, Identifier}

case class ContainerId(value: String) extends Identifier[String]

case class Container
(
  id: String,
  name: String,
  description: String) extends Entity[ContainerId]
