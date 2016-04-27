package com.arielnetworks.ragnalog.port.adapter.http

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.RouteResult._
import akka.stream.ActorMaterializer
import com.arielnetworks.ragnalog.application.archive.data.GetContainersResult
import spray.json.DefaultJsonProtocol._

import scala.io.StdIn

object WebServer extends App  {

  implicit val system = ActorSystem("my-system")
  implicit val materializer = ActorMaterializer()
  implicit val executionContext = system.dispatcher

  implicit val containersFormat = jsonFormat2(GetContainersResult)

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

