package com.arielnetworks.ragnalog.port.adapter.http

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.server.Directives._
import akka.stream.Materializer
import com.arielnetworks.ragnalog.application.AdministrationService
import com.arielnetworks.ragnalog.application.container.data.{AddContainerRequest, ContainerResponse, GetContainersResult}
import com.arielnetworks.ragnalog.domain.model.container.ContainerService
import com.arielnetworks.ragnalog.domain.model.rawfile.RawFileService
import com.arielnetworks.ragnalog.port.adapter.http.route.RouteService
import com.arielnetworks.ragnalog.port.adapter.persistence.repository.ContainerRepositoryOnElasticsearch
import com.arielnetworks.ragnalog.port.adapter.service.{EmbulkAdapter, KibanaAdapter}
import com.arielnetworks.ragnalog.port.adapter.specification.ElasticsearchIdPatternSpecification
import com.sksamuel.elastic4s.{ElasticClient, ElasticsearchClientUri}
import org.elasticsearch.common.settings.Settings
import spray.json._

import scala.concurrent.ExecutionContext
import scala.util.{Failure, Success}


trait AddContainerSupport extends DefaultJsonProtocol with SprayJsonSupport {

  implicit val addContainerRequestFormat = jsonFormat3(AddContainerRequest)
  implicit val addContainerResponseFormat = jsonFormat4(ContainerResponse)

  implicit val containersFormat = jsonFormat1(GetContainersResult)
}

class ContainerRoute extends RouteService with AddContainerSupport {

  //TODO: move to application layer
  val elasticsearchSettings = Settings.settingsBuilder().put("cluster.name", "ragnalog2.elasticsearch")
  implicit val elasticClient = ElasticClient.transport(elasticsearchSettings.build, ElasticsearchClientUri("elasticsearch://localhost:9300"))
  val idSpec = new ElasticsearchIdPatternSpecification
  val containerRepository = new ContainerRepositoryOnElasticsearch(elasticClient)
  val containerService = new ContainerService(containerRepository)
  val visualizationAdapter = new KibanaAdapter
  val registrationAdapter = new EmbulkAdapter
  val logFileService = new RawFileService
  val administrationService = new AdministrationService(containerService, visualizationAdapter, registrationAdapter, logFileService, idSpec)

  def route(implicit m: Materializer, ec: ExecutionContext) =
    pathPrefix("containers") {
      pathEndOrSingleSlash {
        get {
          // get containers
          complete {
            administrationService.activeContainers()
              .map(list => list.map(c => new ContainerResponse(c.id.value, c.name, c.description, c.status.toString).toJson))
          }
        } ~
          (post & entity(as[AddContainerRequest])) { req =>
            println(s"add container: $req")
            val container = administrationService.createContainer(Some(req.id), Some(req.name), req.description)

            // create new container
            onComplete(container) {
              case Success(c) => complete(new ContainerResponse(c.id.value, c.name, c.description, c.status.toString).toJson)
              case Failure(e) =>
                println("failed to add container")
                complete(500 -> "failed to add container")
            }
          }
      } ~
        path(Segment) { containerId =>
          get {
            // get container
            complete(s"get $containerId")
          } ~
            put {
              // update container
              complete(s"update $containerId")
            } ~
            delete {
              // delete container
              complete(s"delete $containerId")
            }
        }
    }
}
