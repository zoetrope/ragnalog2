package com.arielnetworks.ragnalog.domain.model.archive

import com.arielnetworks.ragnalog.domain.model.common.DomainEvent
import LogFile

case class FileAdded(status: String, archiveId: String, file: LogFile) extends DomainEvent
