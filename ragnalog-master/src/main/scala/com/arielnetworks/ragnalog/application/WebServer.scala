package com.arielnetworks.ragnalog.application

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.stream.ActorMaterializer
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import com.arielnetworks.ragnalog.application.archive.data.GetContainersResult
import scala.io.StdIn
import spray.json._
import spray.json.DefaultJsonProtocol._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._

object WebServer extends CorsSupport {
  def main(args: Array[String]) {


    implicit val system = ActorSystem("my-system")
    implicit val materializer = ActorMaterializer()
    implicit val executionContext = system.dispatcher

    implicit val containersFormat = jsonFormat2(GetContainersResult)

    val route: Route =
      pathSingleSlash {
        get {
          complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, "<h1>Say hello to akka-http</h1>"))
        }
      } ~
        path("api" / "hello") {
          corsHandler {
            get {
              complete(new GetContainersResult("test", "hogehoge").toJson)
            }
          }
        }

    val bindingFuture = Http().bindAndHandle(route, "localhost", 8080)

    println(s"Server online at http://localhost:8080/\nPress RETURN to stop...")
    StdIn.readLine()
    bindingFuture
      .flatMap(_.unbind())
      .onComplete(_ ⇒ system.terminate())
  }
}

