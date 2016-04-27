package com.arielnetworks.ragnalog.port.adapter.http

import java.util.UUID

import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.ws.{Message, TextMessage}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.RouteResult._
import akka.stream.scaladsl.{Flow, GraphDSL, Merge, Sink, Source}
import akka.stream.{ActorMaterializer, FlowShape, OverflowStrategy}
import com.arielnetworks.ragnalog.application.archive.data.GetContainersResult
import spray.json.DefaultJsonProtocol._

import scala.collection.mutable
import scala.io.StdIn

class BroadcastActor extends Actor {

  val subscribers = mutable.HashMap.empty[String, ActorRef]

  def receive: Receive = {
    case Subscribe(id, actorRef) => subscribers += ((id, actorRef))
    case UnSubscribe(id) => subscribers -= id
    case todo => subscribers.values.foreach(_ ! todo)
  }

}

sealed trait ToDoMessage

case class Subscribe(id: String, actorRef: ActorRef) extends ToDoMessage

case class UnSubscribe(id: String) extends ToDoMessage

case class ToDo(title: String) extends ToDoMessage

object WebServer extends App with CorsSupport {

  implicit val system = ActorSystem("my-system")
  implicit val materializer = ActorMaterializer()
  implicit val executionContext = system.dispatcher

  implicit val containersFormat = jsonFormat2(GetContainersResult)

//  val dataPublisherRef = system.actorOf(Props[MyPublisher])
//  val dataPublisher = ActorPublisher[MyData](dataPublisherRef)
//  val dataSource = Source.fromPublisher(dataPublisher)

  //    val props = MyPublisher.props(executionContext)
  //    val dataSource = Source.actorPublisher[MyData](props)

//  val myFlow = Flow.fromSinkAndSource(Sink.ignore, dataSource map { d => TextMessage.Strict(d.data) })


  val broadcastActor = system.actorOf(Props[BroadcastActor])

  import GraphDSL.Implicits._
  val foldFlow: Flow[Int, Int, scala.concurrent.Future[Int]] = Flow.fromGraph(GraphDSL.create(Sink.fold[Int, Int](0)(_ + _)) {
    implicit builder ⇒
      fold ⇒
        FlowShape(fold.in, builder.materializedValue.mapAsync(4)(identity).outlet)
  })


  def webSocketFlow = Flow.fromGraph(GraphDSL.create(Source.actorRef[ToDo](bufferSize = 3, OverflowStrategy.fail)) {
    implicit builder =>
      subscribeSource =>

        println(s"source: $subscribeSource")
        import GraphDSL.Implicits._

        val fromWebSocketFlow = builder.add(Flow[Message].collect {
          case TextMessage.Strict(text) => ToDo(text)
        })

        val merge = builder.add(Merge[ToDoMessage](2))
        val uuid = UUID.randomUUID().toString
        val acceptanceFlow = builder.materializedValue.map[ToDoMessage](newActor => Subscribe(uuid, newActor))
        val broadcastActorSink = Sink.actorRef(broadcastActor, UnSubscribe(uuid))

        val toWebSocketFlow = builder.add(
          Flow[ToDo].map {
            case t: ToDo =>
              TextMessage(t.title)
          }
        )

        fromWebSocketFlow ~> Flow[ToDoMessage].map(x => {println(s"!!websocket: $x"); x}) ~> merge ~> broadcastActorSink
        acceptanceFlow ~> Flow[ToDoMessage].map(x => {println(s"!!acceptance: $x"); x}) ~> merge
        subscribeSource ~> Flow[ToDo].map(x => {println(s"!!subscribe: $x"); x}) ~> toWebSocketFlow
        FlowShape(fromWebSocketFlow.in, toWebSocketFlow.out)
  })


  val route: Route =
    pathSingleSlash {
      get {
        getFromResource("/web/index.html")
      }
    } ~
      path("socket") {
        get {
          handleWebSocketMessages(webSocketFlow)
        }
      } ~
      path("notify") {
        get {
          //            system.eventStream.publish(MyData("teset"))
          broadcastActor ! ToDo("hogeeeeeeeee")
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

