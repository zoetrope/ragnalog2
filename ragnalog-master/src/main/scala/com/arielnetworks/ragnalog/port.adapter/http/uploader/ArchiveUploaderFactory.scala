package com.arielnetworks.ragnalog.port.adapter.http.uploader

import scalax.file.Path

object ArchiveUploaderFactory {

  val uploaders = new scala.collection.mutable.HashMap[Path, ArchiveUploader]

  def getOrCreate(filePath: Path): ArchiveUploader = {
    uploaders.synchronized {
      uploaders.getOrElseUpdate(filePath, new ArchiveUploader(filePath))
    }
  }

}
