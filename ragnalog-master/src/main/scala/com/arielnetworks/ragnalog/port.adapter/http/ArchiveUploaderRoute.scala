package com.arielnetworks.ragnalog.port.adapter.http

import akka.http.scaladsl.model.Multipart
import akka.http.scaladsl.server.Directives._
import com.arielnetworks.ragnalog.application.archive.data.GetContainersResult
import spray.json.DefaultJsonProtocol._

class ArchiveUploaderRoute extends RouteService {

  implicit val containersFormat = jsonFormat2(GetContainersResult)

  def route =
    pathPrefix("archive") {
      entity(as[Multipart.FormData]) { (formData: Multipart.FormData) ⇒
        println(formData)
        complete {
          "ok post"
        }
      }
    }
}
