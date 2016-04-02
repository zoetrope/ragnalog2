package com.arielnetworks.ragnalog.domain.model.container

import com.arielnetworks.ragnalog.domain.model.common.DomainEvent

case class ContainerAdded(status: String, container: Container) extends DomainEvent
