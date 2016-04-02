package com.arielnetworks.ragnalog.domain.model.archive

import com.arielnetworks.ragnalog.domain.model.common.DomainEvent

case class FileAdded(status: String, archiveId: String, file: ArchiveFile) extends DomainEvent
