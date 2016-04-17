package com.arielnetworks.ragnalog.application.monitoring

import com.sksamuel.elastic4s.ElasticClient
import com.sksamuel.elastic4s.ElasticDsl._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

//TODO: bad naming
//TODO: this code should be Port.Adapter layer
class Statistics(elasticClient: ElasticClient) {

  def get(command: GetStatisticsCommand): Future[IndexStatistics] = {
    for {
      result <- elasticClient.execute {
        search in command.index / command.typename query all aggregations(
          aggregation min "min_date" field command.timeField,
          aggregation max "max_date" field command.timeField
          )
      }
      count = result.totalHits
      min = result.aggregations.get("max_date")
      max = result.aggregations.get("max_date")
      statistics = new IndexStatistics(count, min, max)
    } yield statistics
  }
}
