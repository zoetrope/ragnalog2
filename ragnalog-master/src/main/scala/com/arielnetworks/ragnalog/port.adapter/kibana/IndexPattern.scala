package com.arielnetworks.ragnalog.port.adapter.kibana

import com.sksamuel.elastic4s.ElasticClient
import org.elasticsearch.action.ActionListener
import org.elasticsearch.action.admin.indices.mapping.get.GetFieldMappingsResponse
import org.elasticsearch.action.support.IndicesOptions

import scala.concurrent.{Future, Promise}

class IndexPattern(elasticClient: ElasticClient) {

  def update(): Future[Unit] = ???

  def remove() = ???


  private def fieldMappings(index: String): Future[GetFieldMappingsResponse] = {
    val builder = elasticClient.admin.indices.prepareGetFieldMappings(index)
    builder.setFields("*")
    builder.setIndicesOptions(IndicesOptions.fromOptions(false, false, true, false))

    val p = Promise[GetFieldMappingsResponse]
    builder.execute(new ActionListener[GetFieldMappingsResponse] {
      override def onResponse(response: GetFieldMappingsResponse): Unit = {
        p.success(response)
      }

      override def onFailure(e: Throwable): Unit = {
        p.failure(e)
      }
    })
    p.future
  }
}
