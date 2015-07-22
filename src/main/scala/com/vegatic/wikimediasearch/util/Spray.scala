package com.vegatic.wikimediasearch.util

import spray.http.CacheDirectives._
import spray.http.HttpHeaders.`Cache-Control`
import spray.http._
import spray.routing.Route
import spray.routing.directives.RespondWithDirectives._

object Spray {

  def jsonMedia(route: Route) = respondWithMediaType(MediaTypes.`application/json`) {
    route
  }
  def htmlMedia(route: Route) = respondWithMediaType(MediaTypes.`text/html`){
    route
  }

  def noCache(route: Route) = respondWithHeader(`Cache-Control`(`must-revalidate`, `no-store`, `no-cache`)) {
    route
  }
}


