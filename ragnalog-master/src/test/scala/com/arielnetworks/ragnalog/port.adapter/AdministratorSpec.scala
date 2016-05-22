package com.arielnetworks.ragnalog.port.adapter

import com.arielnetworks.ragnalog.port.adapter.service.AdministratorOnElasticsearch
import com.arielnetworks.ragnalog.test.ElasticsearchTestSupport
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.time.{Seconds, Span}
import org.scalatest.{BeforeAndAfterAll, DiagrammedAssertions, FunSpec}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Await, Future}
import scala.io.Source

class AdministratorSpec extends FunSpec with DiagrammedAssertions with ScalaFutures with BeforeAndAfterAll with ElasticsearchTestSupport {

  val administrator = new AdministratorOnElasticsearch(elasticClient)

  val indexName = ".ragnalog2_test"
  val templateName = "template_ragnalog_test"

  private def initialize() = {
    val f = for {
      existIndex <- administrator.existsIndex(indexName)
      _ <- if (existIndex) {
        administrator.deleteIndex(indexName)
      } else {
        Future.successful(())
      }

      existTemplate <- administrator.existsTemplate(templateName)
      _ <- if (existTemplate) {
        administrator.deleteTemplate(templateName)
      } else {
        Future.successful(())
      }
    } yield ()

    Await.result(f, Span(5, Seconds))
  }

  override def beforeAll(): Unit = {
    initialize()
  }

  override def afterAll(): Unit = {
    initialize()
  }

  describe("mapping") {
    it("should create mapping") {
      val mapping = Source.fromURL(administrator.getClass.getClassLoader.getResource("elasticsearch/mappings.json"))
        .getLines()
        .mkString(System.lineSeparator())

      val f = for {
        exist <- administrator.existsIndex(indexName)
        _ = assert(exist === false)

        _ <- administrator.createMapping(indexName, mapping)

        exist <- administrator.existsIndex(indexName)
        _ = assert(exist === true)
      } yield ()

      whenReady(f, timeout(Span(3, Seconds))) { result =>
        assert(true)
      }
    }
  }

  describe("template") {
    it("should create template") {
      val template = Source.fromURL(administrator.getClass.getClassLoader.getResource("elasticsearch/template.json"))
        .getLines()
        .mkString(System.lineSeparator())

      val f = for {
        exist <- administrator.existsTemplate(templateName)
        _ = assert(exist === false)

        _ <- administrator.createTemplate(templateName, template)

        exist <- administrator.existsTemplate(templateName)
        _ = assert(exist === true)
      } yield ()

      whenReady(f, timeout(Span(3, Seconds))) { result =>
        assert(true)
      }
    }
  }

}
