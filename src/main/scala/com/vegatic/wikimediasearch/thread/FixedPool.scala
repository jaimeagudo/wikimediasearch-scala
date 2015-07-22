package com.vegatic.wikimediasearch.thread

import java.util.concurrent.Executors
import com.typesafe.config.ConfigFactory
import scala.concurrent._

object FixedPool {

  val conf = ConfigFactory.load()
  val maxSize = conf.getInt("futurePool.maxSize")

  object Implicits extends scala.AnyRef {
    implicit val context = scala.concurrent.ExecutionContext.fromExecutorService(Executors.newFixedThreadPool(maxSize))
  }

  def Future[T](body: => T): Future[T] = Future {
    body
  }
}