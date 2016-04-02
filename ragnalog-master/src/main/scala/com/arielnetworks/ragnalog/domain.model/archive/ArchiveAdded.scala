package com.arielnetworks.ragnalog.domain.model.archive

import com.arielnetworks.ragnalog.domain.model.common.DomainEvent

case class ArchiveAdded(status: String, archive: Archive) extends DomainEvent
