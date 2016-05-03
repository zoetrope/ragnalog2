package com.arielnetworks.ragnalog.support

import java.net.URLDecoder

import org.scalatest.{DiagrammedAssertions, FunSpec}

import scala.io.Source
import scalax.file.Path

class ArchiveUtilSpec extends FunSpec with DiagrammedAssertions {

  describe("getFileList") {
    describe("plain text file") {
      it("should be expanded") {
        val path = Path(getClass.getClassLoader.getResource("expander/a.txt").getPath, '/')
        val list = ArchiveUtil.getFileList(path)
        assert(list == List("a.txt"))
      }
    }
    describe("gzip") {
      it("should be expanded") {
        val path = Path(getClass.getClassLoader.getResource("expander/c.txt.gz").getPath, '/')
        val list = ArchiveUtil.getFileList(path)
        assert(list == List("c.txt.gz/c.txt"))
      }
    }
    describe("tar") {
      it("should be expanded") {
        val path = Path(getClass.getClassLoader.getResource("expander/z.tar").getPath, '/')
        val list = ArchiveUtil.getFileList(path)
        assert(list === List(
          "z/flat.tar.gz/a.txt",
          "z/flat.tar.gz/b.txt",
          "z/c.txt.gz/c.txt",
          "z/a.txt",
          "z/flat.zip/a.txt",
          "z/flat.zip/b.txt",
          "z/flat.tar/a.txt",
          "z/flat.tar/b.txt",
          "z/b.txt"))
      }
    }
    describe("zip") {
      it("should be expanded") {
        val path = Path(getClass.getClassLoader.getResource("expander/z.zip").getPath, '/')
        val list = ArchiveUtil.getFileList(path)
        assert(list == List(
          "z/flat.tar.gz/a.txt",
          "z/flat.tar.gz/b.txt",
          "z/a.txt",
          "z/flat.zip/a.txt",
          "z/flat.zip/b.txt",
          "z/flat.tar/a.txt",
          "z/flat.tar/b.txt",
          "z/b.txt",
          "z/c.txt.gz/c.txt"))
      }
    }
    describe("tgz") {
      it("should be expanded") {
        val path = Path(getClass.getClassLoader.getResource("expander/z.tar.gz").getPath, '/')
        val list = ArchiveUtil.getFileList(path)
        assert(list == List(
          "z/flat.tar.gz/a.txt",
          "z/flat.tar.gz/b.txt",
          "z/c.txt.gz/c.txt",
          "z/a.txt",
          "z/flat.zip/a.txt",
          "z/flat.zip/b.txt",
          "z/flat.tar/a.txt",
          "z/flat.tar/b.txt",
          "z/b.txt"))
      }
    }
    describe("japanese name file created by windows") {
      it("should be expanded") {
        val path = Path(getClass.getClassLoader.getResource("expander/日本語(windows).zip").getPath, '/')
        val list = ArchiveUtil.getFileList(path)
        assert(list == List("日本語(windows)/a配下/テストa.txt", "日本語(windows)/テストb.txt"))
      }
    }
    describe("japanese name file created by linux") {
      it("should be expanded") {
        val path = Path(getClass.getClassLoader.getResource("expander/日本語(linux).tar.gz").getPath, '/')
        val list = ArchiveUtil.getFileList(path)
        assert(list == List("日本語(linux)/テストb.txt", "日本語(linux)/a配下/テストa.txt"))
      }
    }
  }
  describe("getTargetStream") {
    describe("plain text file") {
      it("should be extracted") {
        val path = Path(getClass.getClassLoader.getResource("expander/z.tar.gz").getPath, '/')
        val inputStream = ArchiveUtil.getTargetStream(path, "z/a.txt")
        val doc = inputStream match {
          case Some(s) => Source.fromInputStream(s).getLines().toList
          case _ => fail()
        }
        assert(doc == List("aaaa"))
      }
    }
    describe("gzip") {
      it("should be extracted") {
        val path = Path(getClass.getClassLoader.getResource("expander/z.tar.gz").getPath, '/')
        val inputStream = ArchiveUtil.getTargetStream(path, "z/c.txt.gz/c.txt")
        val doc = inputStream match {
          case Some(s) => Source.fromInputStream(s).getLines().toList
          case _ => fail()
        }
        assert(doc == List("ccccc"))
      }
    }
    describe("tar") {
      it("should be extracted") {
        val path = Path(getClass.getClassLoader.getResource("expander/z.tar.gz").getPath, '/')
        val inputStream = ArchiveUtil.getTargetStream(path, "z/flat.tar/b.txt")
        val doc = inputStream match {
          case Some(s) => Source.fromInputStream(s).getLines().toList
          case _ => fail()
        }
        assert(doc == List("bbbb"))
      }
    }
    describe("zip") {
      it("should be extracted") {
        val path = Path(getClass.getClassLoader.getResource("expander/z.tar.gz").getPath, '/')
        val inputStream = ArchiveUtil.getTargetStream(path, "z/flat.zip/b.txt")
        val doc = inputStream match {
          case Some(s) => Source.fromInputStream(s).getLines().toList
          case _ => fail()
        }
        assert(doc == List("bbbb"))
      }
    }
    describe("tgz") {
      it("should be extracted") {
        val path = Path(getClass.getClassLoader.getResource("expander/z.tar.gz").getPath, '/')
        val inputStream = ArchiveUtil.getTargetStream(path, "z/flat.tar.gz/b.txt")
        val doc = inputStream match {
          case Some(s) => Source.fromInputStream(s).getLines().toList
          case _ => fail()
        }
        assert(doc == List("bbbb"))
      }
    }
  }

  describe("exception") {
    describe("not found file") {

    }
    describe("not found target") {

    }
  }

}
