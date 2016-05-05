package com.arielnetworks.ragnalog.domain.model.visualization

import com.arielnetworks.ragnalog.domain.model.container.Container
import com.arielnetworks.ragnalog.domain.model.logfile.LogFile

import scala.concurrent.Future

trait VisualizationAdapter {

  def activate(logFile: LogFile): Future[Unit] = ???

  def deactivate(logFile: LogFile): Future[Unit] = ???


}
