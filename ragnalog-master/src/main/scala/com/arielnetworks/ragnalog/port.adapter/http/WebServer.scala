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
import com.arielnetworks.ragnalog.port.adapter.service.{DispatcherActor, RegistrationJob}
import com.typesafe.config.ConfigFactory
import org.slf4j.LoggerFactory

import scala.concurrent.Future
import scala.concurrent.duration._
import scala.io.StdIn

object WebServer extends App {

  implicit val system: ActorSystem = ActorSystem("ragnalog-master")

  implicit val materializer = ActorMaterializer()
  implicit val executionContext = system.dispatcher

  implicit val timeout = Timeout(100.millisecond)
  val dispatcherActor = system.actorOf(Props[DispatcherActor])

  val registrationActorPath = "akka.tcp://ragnalog-node@0.0.0.0:2552/user/registration"

  val registrationRef = system.actorSelection(registrationActorPath)

  val logger = LoggerFactory.getLogger("WebServer")

  logger.info("logging test")

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
          //          val reply: Future[String] = (dispatcherActor ? RegistrationJob).mapTo[String]
          registrationRef ! "testtest"
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

  val port = 8686
  val bindingFuture = Http().bindAndHandle(route2HandlerFlow(route), "localhost", port)

  println(s"Server online at http://localhost:$port/\nPress RETURN to stop...")
  StdIn.readLine()
  bindingFuture
    .flatMap(_.unbind())
    .onComplete(_ ⇒ system.terminate())

}

