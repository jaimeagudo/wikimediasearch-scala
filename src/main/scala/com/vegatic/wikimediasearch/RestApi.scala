package com.vegatic.wikimediasearch


import akka.actor.{Actor, Props}
import com.vegatic.wikimediasearch.indexer.{DocsIndex, Core}
import com.vegatic.wikimediasearch.interfaces.{SearchResponse, SearchResponseImplicits}
import com.vegatic.wikimediasearch.thread.FixedPool
import com.vegatic.wikimediasearch.util.Spray._
import spray.routing.{HttpService, Route}

object RestApiActor {
  def props = Props[RestApiActor]

  def name = "restApiActor"

}

class RestApiActor(docsIndex: DocsIndex)
  extends Actor with HttpService {


  // Get the metrics spray routing aware directive factory
  val time = Boot.metricsFactory.timer("searchRequest").time
  val counter = Boot.metricsFactory.counter("searchRequest").all.count
  val counterFailures = Boot.metricsFactory.counter("searchRequest").failures.count
  // Join the metrics together saying, I'm monitoring both the time and num of requests for GET("/estimate")
  val account = time & counter


  val coreRoute: Route = {
    import SearchResponseImplicits._
    import spray.httpx.SprayJsonSupport.{sprayJsonMarshaller, sprayJsonUnmarshaller}

    path("search") {
      jsonMedia {
        get {
          account {
            parameters('q) {q =>
              //              detach(FixedPool.Implicits.context) {
              complete {
                SearchResponse(q, Core.search(q, docsIndex))
              }
              //                } else counterFailures {
              //                  complete {
              //                    HttpResponse(StatusCodes.ServiceUnavailable)
              //                  }


            }
          }
        }
      }
    }
  }

  def actorRefFactory = context

  def receive = runRoute(coreRoute)
}



