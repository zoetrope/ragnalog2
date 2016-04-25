package com.arielnetworks.ragnalog.application

import akka.actor.{ActorSystem, Props}
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.ws.{BinaryMessage, Message, TextMessage}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.RouteResult._
import akka.stream.ActorMaterializer
import akka.stream.actor.ActorPublisher
import akka.stream.scaladsl.{Flow, Sink, Source}
import com.arielnetworks.ragnalog.application.archive.data.GetContainersResult
import com.arielnetworks.ragnalog.application.http.{MyData, MyPublisher}
import spray.json.DefaultJsonProtocol._

import scala.io.StdIn

object WebServer extends CorsSupport {
  def main(args: Array[String]) {

    implicit val system = ActorSystem("my-system")
    implicit val materializer = ActorMaterializer()
    implicit val executionContext = system.dispatcher

    implicit val containersFormat = jsonFormat2(GetContainersResult)

    val dataPublisherRef = system.actorOf(Props[MyPublisher])
    val dataPublisher = ActorPublisher[MyData](dataPublisherRef)
    val dataSource = Source.fromPublisher(dataPublisher)

//    val props = MyPublisher.props(executionContext)
//    val dataSource = Source.actorPublisher[MyData](props)

    val myFlow = Flow.fromSinkAndSource(Sink.ignore, dataSource map { d => TextMessage.Strict(d.data) })

    val route: Route =
      pathSingleSlash {
        get {
          getFromResource("/web/index.html")
        }
      } ~
        path("socket") {
          get {
            handleWebSocketMessages(myFlow)
          }
        } ~
        path("notify") {
          get {
//            system.eventStream.publish(MyData("teset"))
            dataPublisherRef ! MyData("hogeeeeeeeee")
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

    val bindingFuture = Http().bindAndHandle(route2HandlerFlow(route), "localhost", 8686)

    println(s"Server online at http://localhost:8080/\nPress RETURN to stop...")
    StdIn.readLine()
    bindingFuture
      .flatMap(_.unbind())
      .onComplete(_ ⇒ system.terminate())
  }
}

