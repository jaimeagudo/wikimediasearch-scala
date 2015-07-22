package com.vegatic.wikimediasearch


package object indexer {


case class TitleAbstract(title: String, `abstract`: String) {
  require(!title.isEmpty && !`abstract`.isEmpty)
}

case class DocsIndex(docsByUrl: scala.collection.immutable.HashMap[String, TitleAbstract],
                     urlsByWord: scala.collection.immutable.HashMap[String, Set[String]])

}
