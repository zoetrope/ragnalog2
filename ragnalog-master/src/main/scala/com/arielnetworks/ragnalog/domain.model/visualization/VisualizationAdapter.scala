package com.arielnetworks.ragnalog.domain.model.visualization

import com.arielnetworks.ragnalog.domain.model.container.Container

import scala.concurrent.Future

trait VisualizationAdapter {

  def addContainer(container: Container): Future[Unit] = ???

  def removeContainer(container: Container): Future[Unit] = ???
}
