package com.arielnetworks.ragnalog.support

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success, Try}

trait FutureSupport {
  def sequenceIgnoreFailure[A](f: Future[A]*)(implicit ex: ExecutionContext): Future[Seq[A]] = {
    Future.sequence(f.map(_.map(Some(_)).recover { case t => None })).map(_.flatten)
  }

  def sequenceTryAll[A](f: Future[A]*)(implicit ex: ExecutionContext): Future[Seq[Try[A]]] = {
    Future.sequence(f.map(_.map(Success(_)).recover { case t => Failure(t) }))
  }
}

object FutureSupport extends FutureSupport
