package com.arielnetworks.ragnalog.port.adapter.http

import akka.actor._
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.RouteResult._
import akka.util.Timeout
import com.arielnetworks.ragnalog.application.{InvokeRegistrationMessage, ServiceRegistry}
import com.arielnetworks.ragnalog.port.adapter.http.notification.{WSMessage, WebSocketSupport}
import com.arielnetworks.ragnalog.port.adapter.http.route.RestRoute
import com.typesafe.config.ConfigFactory
import org.joda.time.DateTime
import org.slf4j.LoggerFactory

import scala.io.StdIn

object WebServer extends App {

  implicit val system = ServiceRegistry.actorSystem
  implicit val materializer = ServiceRegistry.materializer
  implicit val executionContext = system.dispatcher


  val logger = LoggerFactory.getLogger("WebServer")

  val config = ConfigFactory.load().getConfig("ragnalog-master")
  logger.info(s"config = ${config}")

  val socket = new WebSocketSupport(system)

  val registrationService = ServiceRegistry.registrationAdapter

  val route: Route =
    pathSingleSlash {
      get {
        getFromResource("/web/index.html")
      }
    } ~
      (new RestRoute).route ~
      path("socket") {
        get {
          handleWebSocketMessages(socket.webSocketFlow)
        }
      } ~
      path("registration") {
        get {
          registrationService.invoke(
            new InvokeRegistrationMessage("containerId", "archiveId", "archiveFileName", "filePath", "logType", "extra", "index", 1, new DateTime().toString, null))
          complete("ok")
        }
      } ~
      path("long" / IntNumber) { time =>
        get {
          println(s"start $time")
          Thread.sleep(time)
          println(s"end $time")
          complete("ok")
        }
      } ~
      path("notify") {
        get {
          //            system.eventStream.publish(MyData("teset"))
          socket.broadcastActor ! WSMessage("hogeeeeeeeee")
          complete("ok")
        }
      } ~
      pathPrefix("web") {
        path("index.html") {
          getFromResource("web/index.html")
        } ~
          path("bundle.js") {
            getFromResource("web/bundle.js")
          } ~
          path("main.css") {
            getFromResource("main.css")
          }
      } ~
      getFromResourceDirectory("web")

  val host = config.getString("http.hostname")
  val port = config.getInt("http.port")
  val bindingFuture = Http().bindAndHandle(route2HandlerFlow(route), host, port)

  println(s"Server online at http://$host:$port/\nPress RETURN to stop...")
  StdIn.readLine()
  bindingFuture
    .flatMap(_.unbind())
    .onComplete(_ ⇒ system.terminate())

}

