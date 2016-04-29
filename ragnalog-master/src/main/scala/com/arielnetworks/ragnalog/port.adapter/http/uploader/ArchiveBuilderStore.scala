package com.arielnetworks.ragnalog.port.adapter.http.uploader

import scalax.file.Path

object ArchiveBuilderStore {

  val uploaders = new scala.collection.mutable.HashMap[Path, ArchiveBuilder]

  def getOrCreate(filePath: Path): ArchiveBuilder = {
    uploaders.synchronized {
      uploaders.getOrElseUpdate(filePath, new ArchiveBuilder(filePath))
    }
  }

  def remove(filePath: Path) = {
    uploaders.synchronized {
      if (uploaders.contains(filePath)) {
        uploaders.remove(filePath)
      }
    }
  }
}
