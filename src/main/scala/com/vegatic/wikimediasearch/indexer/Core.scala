package com.vegatic.wikimediasearch.indexer

import com.typesafe.scalalogging.LazyLogging
import com.vegatic.wikimediasearch.interfaces.{Result}

import scala.xml._

object Core extends LazyLogging {

  def URL_PREFIX = "http://en.wikipedia.org/wiki/"

  def storeAndIndex(filename: String): DocsIndex = {

    var start = System.currentTimeMillis()
    val docsByUrl = filterDocs(XML.loadFile(filename))
    var time = System.currentTimeMillis() - start
    println("Filtering Execution time: " + time / 1000 + "s")

    start = System.currentTimeMillis()
    val urlsByWord = indexDocs(docsByUrl)
    time = System.currentTimeMillis() - start
    println("Indexing Execution time: " + time /1000 + "s")

    DocsIndex(docsByUrl, urlsByWord)
  }

  def search(q: String, docsIndex: DocsIndex): Array[Result] = {

    val slugs = docsIndex.urlsByWord.get(q).getOrElse(Set[String]()).toArray

    slugs.flatMap(slug => {
      docsIndex.docsByUrl.get(slug).map(ta =>
        Result(ta.title, URL_PREFIX + slug, ta.`abstract`))
    })
  }

  def indexDocs(docs: scala.collection.immutable.HashMap[String, TitleAbstract]): scala.collection.immutable.HashMap[String, Set[String]] = {

    var urlsByWord = scala.collection.immutable.HashMap[String, Set[String]]()

    //TODO use fold
    for (doc <- docs) {
      val ta: TitleAbstract = doc._2
      val url: String = doc._1

      val text = ta.title + " " + ta.`abstract`
      //TODO use better tokenizer
      val docWords = text.split(" ").distinct
      for (word <- docWords) {
        val urls: Set[String] = urlsByWord.get(word).getOrElse(Set[String]())
        urlsByWord += (word -> (urls + url))
      }
    }
    urlsByWord
  }

  def filterDocs(xmlElem: Elem): scala.collection.immutable.HashMap[String, TitleAbstract] = {


    var docs = scala.collection.immutable.HashMap[String, TitleAbstract]()

    //TODO use fold
    for (xmldoc <- xmlElem \\ "doc") {
      try {
        val doc = new TitleAbstract((xmldoc \\ "title").text, (xmldoc \\ "abstract").text)
        val entryKey = (xmldoc \\ "url").text.replace(URL_PREFIX, "")
        if (!entryKey.isEmpty())
          docs += (entryKey -> doc)
      } catch {
        case t: Throwable =>
          logger.error(s"Empty doc discarded. ${t.getMessage}")
      }
    }
    docs
  }
}