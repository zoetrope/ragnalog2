package com.arielnetworks.ragnalog.domain.model.archive

//TODO: This class should not be here
sealed trait ArchiveType {
  def fromExtension(fileName: String): ArchiveType = {
    if (fileName.endsWith(".zip")) Zip
    else if (fileName.endsWith(".tar")) Tar
    else if (fileName.endsWith(".tar.gz") || fileName.endsWith(".tgz")) Tgz
    else if (fileName.endsWith(".gz")) GZip
    else None
  }

  def of(value: String): ArchiveType = value match {
    case "Zip" => Zip
    case "Tar" => Tar
    case "Tgz" => Tgz
    case "GZip" => GZip
    case _ => None
  }

  def isArchive: Boolean = this match {
    case Zip | Tar | Tgz => true
    case _ => false
  }

  def isTar: Boolean = this match {
    case Tar | Tgz => true
    case _ => false
  }

  def isGZip: Boolean = this match {
    case GZip | Tgz => true
    case _ => false
  }

  def getArchiverName(): String = this match {
    case Zip => "zip"
    case Tar | Tgz => "tar"
    case _ => "unknown"
  }
}

object ArchiveType extends ArchiveType

case object None extends ArchiveType

case object Zip extends ArchiveType

case object Tgz extends ArchiveType

case object Tar extends ArchiveType

case object GZip extends ArchiveType
