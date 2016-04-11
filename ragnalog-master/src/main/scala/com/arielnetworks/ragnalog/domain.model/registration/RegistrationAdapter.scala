package com.arielnetworks.ragnalog.domain.model.registration

import com.arielnetworks.ragnalog.domain.model.container.Container

import scala.concurrent.Future

trait RegistrationAdapter {
  def open(container: Container): Future[Unit] = ???

  def close(container: Container): Future[Unit] = ???


  def remove(container: Container): Future[Unit] = ???

}
