package com.arielnetworks.ragnalog.application

import com.arielnetworks.ragnalog.domain.model.archive.ArchiveId
import com.arielnetworks.ragnalog.domain.model.container.ContainerId

class UserService {

  def uploadArchiveFile() = ???

  def removeArchiveFile() = ???

  def registerLogFile() = ???

  def unregisterLogFile() = ???

  def archives(containerId: ContainerId) = ???

  def logFiles(archiveId: ArchiveId) = ???
}
