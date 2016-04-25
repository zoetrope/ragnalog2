package com.arielnetworks.ragnalog.application

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.RouteResult._
import akka.stream.ActorMaterializer
import com.arielnetworks.ragnalog.application.archive.data.GetContainersResult
import com.arielnetworks.ragnalog.application.http.RestRoute
import spray.json.DefaultJsonProtocol._

import scala.io.StdIn

object WebServer extends CorsSupport {
  def main(args: Array[String]) {

    implicit val system = ActorSystem("my-system")
    implicit val materializer = ActorMaterializer()
    implicit val executionContext = system.dispatcher

    implicit val containersFormat = jsonFormat2(GetContainersResult)

    val route: Route =
      pathSingleSlash {
        get {
          getFromResource("/web/index.html")
        }
      } ~
        (new RestRoute).route ~
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

    val bindingFuture = Http().bindAndHandle(route2HandlerFlow(route), "localhost", 8686)

    println(s"Server online at http://localhost:8080/\nPress RETURN to stop...")
    StdIn.readLine()
    bindingFuture
      .flatMap(_.unbind())
      .onComplete(_ ⇒ system.terminate())
  }
}

