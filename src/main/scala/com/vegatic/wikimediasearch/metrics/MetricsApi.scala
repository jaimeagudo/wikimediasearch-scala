package com.vegatic.wikimediasearch.metrics

import com.vegatic.wikimediasearch.Boot
import com.vegatic.wikimediasearch.util.Spray._
import akka.actor.{ Actor, Props}
import com.netaporter.salad.metrics.actor.admin.spray.OutputMetricsMessages.OutputMetrics
import com.typesafe.config._

import spray.routing.{HttpService, Route}


object MetricsApiActor {
  def props = Props[MetricsApiActor]

  def name = "metricsActor"

  val conf = ConfigFactory.load()

  def menuPrefix = conf.getString("metrics.menu.prefix")

  def menuHtml = s"""<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\"\n \"http://www.w3.org/TR/html4/loose.dtd\">
       |     <html>
       |        <head>
       |          <title>Metrics</title>
       |        </head>
       |        <body>
       |            <h1>Operational Menu</h1>
       |              <ul>
       |                  <li><a href=\"/$menuPrefix/metrics?pretty=true\">Metrics</a></li>
       |                  <li><a href=\"/$menuPrefix/ping\">Ping</a></li>
       |                  <li><a href=\"/$menuPrefix/threads\">Threads</a></li>
       |                  <li><a href=\"/$menuPrefix/healthcheck?pretty=true\">Healthcheck</a></li>
       |              </ul>
       |         </body>
       |    </html>""".stripMargin

}

class MetricsApiActor extends Actor with HttpService {

  val metricsOutputActor = Boot.metricsFactory.defaultMetricsActorFactory.eventTellAdminActor()

  val metricsRoute: Route = {
    path(MetricsApiActor.menuPrefix) {
      get {
        htmlMedia {
          complete(MetricsApiActor.menuHtml)
        }
      }
    } ~ pathPrefix(MetricsApiActor.menuPrefix) {
      path("metrics") {
        get {
          noCache {
            ctx => metricsOutputActor ! OutputMetrics(ctx)
          }
        }
      } ~ path("healthcheck") {
        get {
          noCache {
            ctx => {
              complete("OK") //TODO
            }
          }
        }
      } ~ path("ping") {
        get {
          noCache {
            complete("pong")
          }
        }
      } ~ path("threads") {
        get {
          noCache {
            //TODO mimic https://dropwizard.github.io/metrics/3.1.0/manual/servlets/ for Threads in Scala
            complete("TODO mimic https://dropwizard.github.io/metrics/3.1.0/manual/servlets/ for Threads in Scala")
          }
        }
      }
    }
  }

  def actorRefFactory = context

  def receive = runRoute(metricsRoute)

}



