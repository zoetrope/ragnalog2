package com.arielnetworks.ragnalog.port.adapter.http

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.Materializer
import com.arielnetworks.ragnalog.port.adapter.http.route.RouteService
import com.arielnetworks.ragnalog.port.adapter.http.uploader.ArchiveUploader
import com.typesafe.config.ConfigFactory
import spray.json.{DefaultJsonProtocol, _}

import scala.collection.JavaConverters._
import scala.concurrent.ExecutionContext

case class LogType(id: String, name:String)

trait AppJsonSupport extends DefaultJsonProtocol with SprayJsonSupport {
  implicit val logTypeFormat = jsonFormat2(LogType)
}

class AppRoute extends RouteService with ArchiveUploader with AppJsonSupport {

  val config = ConfigFactory.load().getConfig("ragnalog-master")

  def route(implicit m: Materializer, ec: ExecutionContext): Route =
    path("logtypes") {
      get {
        val logTypes = config.getConfigList("log-types").asScala.map(c=>new LogType(c.getString("id"), c.getString("name"))).toSeq
        complete(logTypes.map(_.toJson))
      }
    }
}
