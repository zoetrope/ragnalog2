package com.arielnetworks.ragnalog.port.adapter.http.uploader

object ArchiveBuilderStore {

  val uploaders = new scala.collection.mutable.HashMap[(String, String), ArchiveBuilder]

  def getOrCreate(containerId: String, identifier: String): ArchiveBuilder = {
    uploaders.synchronized {
      uploaders.getOrElseUpdate((containerId, identifier), new ArchiveBuilder(containerId, identifier))
    }
  }

  def remove(containerId: String, identifier: String) = {
    uploaders.synchronized {
      if (uploaders.contains((containerId, identifier))) {
        uploaders.remove((containerId, identifier))
      }
    }
  }

  //TODO: clear expired uploaders
}
