package com.arielnetworks.ragnalog.test

import com.sksamuel.elastic4s.ElasticClient
import com.sksamuel.elastic4s.ElasticDsl._
import org.elasticsearch.action.delete.DeleteResponse

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Await, Future}
import scala.concurrent.duration._

trait RepositoryTestSupport {

  val indexName = ".ragnalog2_test"

  def clearAllDocuments(typeName: String)(implicit elasticClient: ElasticClient) = {
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

}
