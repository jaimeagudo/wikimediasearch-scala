package com.vegatic.wikimediasearch.interfaces

import spray.json.DefaultJsonProtocol

case class Result(title: String, url: String, `abstract`: String)

case class SearchResponse(q: String, results: Array[Result])


object SearchResponseImplicits extends DefaultJsonProtocol {
  implicit val resultResponse = jsonFormat3(Result)
  implicit val searchResponse = jsonFormat2(SearchResponse)
}
