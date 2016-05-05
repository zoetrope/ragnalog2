package com.arielnetworks.ragnalog.port.adapter.service

import com.sksamuel.elastic4s.ElasticClient
import com.sksamuel.elastic4s.ElasticDsl._
import org.elasticsearch.action.ActionListener
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse
import org.elasticsearch.action.admin.indices.template.get.GetIndexTemplatesResponse
import org.elasticsearch.action.admin.indices.template.put.PutIndexTemplateResponse
import org.elasticsearch.action.delete.DeleteResponse

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Future, Promise}
import scala.io.Source
import scala.util.{Failure, Success}

class AdministratorOnElasticsearch
(
  protected val elasticClient: ElasticClient
) {

  //TODO: move to application layer
  def initialize(indexName: String, templateName: String) = {

    for {
      existIndex <- this.existsIndex(indexName)
      _ <- if (!existIndex) {
        val mapping = Source.fromURL(getClass.getClassLoader.getResource("elasticsearch/mappings.json"))
          .getLines()
          .mkString(System.lineSeparator())
        createMapping(indexName, mapping)
      } else {
        Future.successful(())
      }

      existTemplate <- this.existsTemplate(templateName)
      _ <- if (!existTemplate) {
        val template = Source.fromURL(getClass.getClassLoader.getResource("elasticsearch/template.json"))
          .getLines()
          .mkString(System.lineSeparator())
        createTemplate(templateName, template)
      } else {
        Future.successful(())
      }
    } yield ()
  }

  def createMapping(indexName: String, mappingJson: String): Future[Unit] = {
    val p = Promise[Unit]()
    try {
      val ret =
        elasticClient.execute(
          create index indexName source mappingJson
        )

      ret onComplete {
        case Success(r) => p.success(Unit)
        case Failure(e) => p.failure(e)
      }
    } catch {
      case e: Throwable => p.failure(e)
    }
    p.future
  }

  def createTemplate(templateName: String, templateJson: String): Future[Unit] = {
    val builder = elasticClient.admin.indices().preparePutTemplate(templateName)
    builder.setSource(templateJson)

    val p = Promise[PutIndexTemplateResponse]
    builder.execute(new ActionListener[PutIndexTemplateResponse] {
      override def onResponse(response: PutIndexTemplateResponse): Unit = {
        p.success(response)
      }

      override def onFailure(e: Throwable): Unit = {
        p.failure(e)
      }
    })
    p.future.map(_ => ())
  }

  def existsIndex(indexName: String): Future[Boolean] = {
    val builder = elasticClient.admin.indices().prepareExists(indexName)

    val p = Promise[IndicesExistsResponse]
    builder.execute(new ActionListener[IndicesExistsResponse] {
      override def onResponse(response: IndicesExistsResponse): Unit = {
        p.success(response)
      }

      override def onFailure(e: Throwable): Unit = {
        p.failure(e)
      }
    })
    p.future.map(_.isExists)
  }

  def existsTemplate(templateName: String): Future[Boolean] = {
    val builder = elasticClient.admin.indices().prepareGetTemplates(templateName)

    val p = Promise[GetIndexTemplatesResponse]
    builder.execute(new ActionListener[GetIndexTemplatesResponse] {
      override def onResponse(response: GetIndexTemplatesResponse): Unit = {
        p.success(response)
      }

      override def onFailure(e: Throwable): Unit = {
        p.failure(e)
      }
    })
    p.future.map(!_.getIndexTemplates.isEmpty)
  }

  def openIndex() = ???

  def closeIndex() = ???

  def deleteIndex(indexName: String): Future[Unit] = {
    println(s"delete $indexName")
    val future = elasticClient.execute {
      delete index indexName
    }
    future.map(_ => ())
  }

  def deleteTemplate(templateName: String): Future[Unit] = {
    println(s"delete $templateName")

    val future = elasticClient.execute {
      delete template templateName
    }
    future.map(_ => ())
  }

  def deleteAllDocuments(indexName: String, typeName: String): Future[Unit] = {
    val future = for {
      countResult <- elasticClient.execute(search in indexName / typeName query all size 0)
      count = countResult.totalHits
      searchResult <- elasticClient.execute(search in indexName / typeName query all from 0 size count.asInstanceOf[Int])
      deleteFutures: List[Future[DeleteResponse]] = searchResult.hits.map(hit => {
        elasticClient.execute(delete(hit.getId) from indexName / typeName)
      }).toList
      _ <- Future.sequence(deleteFutures)
    } yield ()

    future
  }
}