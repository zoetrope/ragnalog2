package com.arielnetworks.ragnalog.support

import org.scalatest.{DiagrammedAssertions, FunSpec}

import scala.io.Source

class ArchiveUtilSpec extends FunSpec with DiagrammedAssertions {

  describe("getFileList") {
    describe("plain text file") {
      it("should be expanded") {
        val url = getClass.getClassLoader.getResource("expander/a.txt")
        val list = ArchiveUtil.getFileList(url.getPath)
        assert(list == List("a.txt"))
      }
    }
    describe("gzip") {
      it("should be expanded") {
        val url = getClass.getClassLoader.getResource("expander/c.txt.gz")
        val list = ArchiveUtil.getFileList(url.getPath)
        assert(list == List("c.txt.gz/c.txt"))
      }
    }
    describe("tar") {
      it("should be expanded") {
        val url = getClass.getClassLoader.getResource("expander/z.tar")
        val list = ArchiveUtil.getFileList(url.getPath)
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
    describe("zip") {
      it("should be expanded") {
        val url = getClass.getClassLoader.getResource("expander/z.zip")
        val list = ArchiveUtil.getFileList(url.getPath)
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
        val url = getClass.getClassLoader.getResource("expander/z.tar.gz")
        val list = ArchiveUtil.getFileList(url.getPath)
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
  }
  describe("getTargetStream") {
    describe("plain text file") {
      it("should be extracted") {
        val url = getClass.getClassLoader.getResource("expander/z.tar.gz")
        val inputStream = ArchiveUtil.getTargetStream(url.getPath, "z/a.txt")
        val doc = inputStream match {
          case Some(s) => Source.fromInputStream(s).getLines().toList
          case _ => fail()
        }
        assert(doc == List("aaaa"))
      }
    }
    describe("gzip") {
      it("should be extracted") {
        val url = getClass.getClassLoader.getResource("expander/z.tar.gz")
        val inputStream = ArchiveUtil.getTargetStream(url.getPath, "z/c.txt.gz/c.txt")
        val doc = inputStream match {
          case Some(s) => Source.fromInputStream(s).getLines().toList
          case _ => fail()
        }
        assert(doc == List("ccccc"))
      }
    }
    describe("tar") {
      it("should be extracted") {
        val url = getClass.getClassLoader.getResource("expander/z.tar.gz")
        val inputStream = ArchiveUtil.getTargetStream(url.getPath, "z/flat.tar/b.txt")
        val doc = inputStream match {
          case Some(s) => Source.fromInputStream(s).getLines().toList
          case _ => fail()
        }
        assert(doc == List("bbbb"))
      }
    }
    describe("zip") {
      it("should be extracted") {
        val url = getClass.getClassLoader.getResource("expander/z.tar.gz")
        val inputStream = ArchiveUtil.getTargetStream(url.getPath, "z/flat.zip/b.txt")
        val doc = inputStream match {
          case Some(s) => Source.fromInputStream(s).getLines().toList
          case _ => fail()
        }
        assert(doc == List("bbbb"))
      }
    }
    describe("tgz") {
      it("should be extracted") {
        val url = getClass.getClassLoader.getResource("expander/z.tar.gz")
        val inputStream = ArchiveUtil.getTargetStream(url.getPath, "z/flat.tar.gz/b.txt")
        val doc = inputStream match {
          case Some(s) => Source.fromInputStream(s).getLines().toList
          case _ => fail()
        }
        assert(doc == List("bbbb"))
      }
    }
  }

}
