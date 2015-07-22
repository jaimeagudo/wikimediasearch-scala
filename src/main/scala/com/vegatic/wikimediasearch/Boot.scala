package com.vegatic.wikimediasearch

import akka.actor.{ActorSystem, Props}
import akka.io.IO
import akka.pattern.ask
import akka.util.Timeout
import com.netaporter.salad.metrics.spray.metrics.MetricsDirectiveFactory
import com.vegatic.wikimediasearch.indexer.Core
import com.vegatic.wikimediasearch.metrics.MetricsApiActor
//import com.codahale.metrics.health.HealthCheckRegistry
import com.typesafe.config._
import com.typesafe.scalalogging.LazyLogging
import spray.can.Http

import scala.concurrent.duration._

object Boot extends App with LazyLogging {

  val conf = ConfigFactory.load()
  val ip = conf.getString("service.localip")
  val port = conf.getInt("service.port")
  val metricsIp = conf.getString("metrics.localip")
  val metricsPort = conf.getInt("metrics.port")
  val toutSecs =conf.getInt("akka.actor.timeoutsecs")

  implicit val timeout = Timeout(toutSecs.seconds)
  implicit val system = ActorSystem("wikimediasearch")

  val metricsFactory: MetricsDirectiveFactory = MetricsDirectiveFactory()

//  val healthCheckRegistry: HealthCheckRegistry = new HealthCheckRegistry()

//  val url = getClass.getResource("/small.xml")
  val url = getClass.getResource("/enwiki-latest-abstract23.xml")
  val docsAndIndex= Core.storeAndIndex(url.getPath())

  val restApiActor = system.actorOf(Props(new RestApiActor(docsAndIndex)), RestApiActor.name)
  logger.info(s"***************** STARTING API on $ip:$port ******************")
  IO(Http) ? Http.Bind(listener = restApiActor,
    interface = ip,
    port = port)


  val metricsApiActor = system.actorOf(Props(new MetricsApiActor()), MetricsApiActor.name)
  logger.info(s"***************** STARTING METRICS on $metricsIp:$metricsPort ******************")
  IO(Http) ? Http.Bind(listener = metricsApiActor,
    interface = metricsIp,
    port = metricsPort)
}