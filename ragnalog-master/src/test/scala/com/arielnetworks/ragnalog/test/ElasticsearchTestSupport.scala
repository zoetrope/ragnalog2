package com.arielnetworks.ragnalog.test

import com.sksamuel.elastic4s.ElasticDsl._
import com.sksamuel.elastic4s.{ElasticClient, ElasticsearchClientUri}
import org.elasticsearch.action.delete.DeleteResponse
import org.elasticsearch.common.settings.Settings
import org.scalatest.time.{Seconds, Span}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.concurrent.{Await, Future}

trait ElasticsearchTestSupport {
  val elasticsearchSettings = Settings.settingsBuilder().put("cluster.name", "ragnalog2.elasticsearch")
  implicit val elasticClient = ElasticClient.transport(elasticsearchSettings.build, ElasticsearchClientUri("elasticsearch://localhost:9300"))


  def testTimeout = Span(3, Seconds)
}
