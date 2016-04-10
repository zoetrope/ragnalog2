package com.arielnetworks.ragnalog.test

import com.sksamuel.elastic4s.ElasticClient
import com.sksamuel.elastic4s.ElasticDsl._

object TestSupport {

  def clearIndex(elasticClient:ElasticClient) = {

//    elasticClient.execute(
//      delete from ".ragnalog2" / "container" query {}
//    )
  }
}
