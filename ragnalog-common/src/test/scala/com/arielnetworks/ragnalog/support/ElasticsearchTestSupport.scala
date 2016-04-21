package com.arielnetworks.ragnalog.support

import com.sksamuel.elastic4s.{ElasticClient, ElasticsearchClientUri}
import com.sksamuel.elastic4s.ElasticDsl._
import org.elasticsearch.action.delete.DeleteResponse
import org.elasticsearch.common.settings.Settings

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.concurrent.{Await, Future}

trait ElasticsearchTestSupport {
  val elasticsearchSettings = Settings.settingsBuilder().put("cluster.name", "ragnalog2.elasticsearch")
  implicit val elasticClient = ElasticClient.transport(elasticsearchSettings.build, ElasticsearchClientUri("elasticsearch://localhost:9300"))

  def clearAllDocuments(indexName: String, typeName: String) = {
    val future = for {
      countResult <- elasticClient.execute(search in indexName / typeName query all size 0)
      count = countResult.totalHits
      searchResult <- elasticClient.execute(search in indexName / typeName query all from 0 size count.asInstanceOf[Int])
      deleteFutures: List[Future[DeleteResponse]] = searchResult.hits.map(hit => {
        elasticClient.execute(delete(hit.getId) from indexName / typeName)
      }).toList
      _ <- Future.sequence(deleteFutures)
    } yield ()

    Await.result(future, 5.seconds)
  }

  def clearIndex(indexName: String) = {
    val future = elasticClient.execute {
      deleteIndex(indexName)
    }
    Await.result(future, 5.seconds)
  }
}
