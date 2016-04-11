package com.arielnetworks.ragnalog.domain.model.visualization

import com.arielnetworks.ragnalog.domain.model.container.Container

import scala.concurrent.Future

class VisualizationService {

  def addContainer(container: Container): Future[Unit] = ???

  def removeContainer(container: Container): Future[Unit] = ???
}
