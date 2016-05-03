package com.arielnetworks.ragnalog.port.adapter.http

import akka.actor.{ActorSystem, Props, _}
import akka.event.Logging
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.RouteResult._
import akka.pattern.ask
import akka.stream.ActorMaterializer
import akka.util.Timeout
import com.arielnetworks.ragnalog.port.adapter.http.notification.{WSMessage, WebSocketSupport}
import com.arielnetworks.ragnalog.port.adapter.http.route.RestRoute
import com.arielnetworks.ragnalog.port.adapter.service.DispatcherActor
import com.arielnetworks.ragnalog.port.adapter.service.DispatcherActor.RegistrationJob
import com.typesafe.config.ConfigFactory
import org.slf4j.LoggerFactory

import scala.concurrent.Future
import scala.concurrent.duration._
import scala.io.StdIn
import scala.collection.JavaConverters._

object WebServer extends App {

  implicit val system: ActorSystem = ActorSystem("ragnalog-master")

  implicit val materializer = ActorMaterializer()
  implicit val executionContext = system.dispatcher

  implicit val timeout = Timeout(100.millisecond)
  val logger = LoggerFactory.getLogger("WebServer")

  val config = ConfigFactory.load().getConfig("ragnalog-master")
  logger.info(s"config = ${config}")

  val registrationActorPath = config.getStringList("registration.path")
  val registrationRef = registrationActorPath.asScala.map(p => system.actorSelection(p))
  val dispatcherActor = system.actorOf(DispatcherActor.props(registrationRef))
  val socket = new WebSocketSupport(system)

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
          val reply: Future[String] = (dispatcherActor ? RegistrationJob).mapTo[String]
          complete(reply)
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

