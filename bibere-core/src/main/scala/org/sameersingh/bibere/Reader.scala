package org.sameersingh.bibere

import java.io.FileInputStream

/**
 * @author sameer
 */
trait Reader {
  def readPublications(path: String): Publications
}

object JsonReader extends Reader {
  val authorsFile = "authors.json"
  val venuesFile = "venues.json"
  val papersFile = "papers.json"

  def readPublications(path: String) = {
    val result = new Publications
    readVenues(result, path)
    readAuthors(result, path)
    readPapers(result, path)
    result
  }

  def readPapers(pubs: Publications, path: String) {
    for(p <- JacksonWrapper.deserialize[Seq[Paper]](FileUtil.readFile(path + "/" + papersFile)))
      pubs += p
  }

  def readAuthors(pubs: Publications, path: String) {
    for(a <- JacksonWrapper.deserialize[Seq[Author]](FileUtil.readFile(path + "/" + authorsFile)))
      pubs += a
  }

  def readVenues(pubs: Publications, path: String) {
    for(v <- JacksonWrapper.deserialize[Seq[Venue]](FileUtil.readFile(path + "/" + venuesFile)))
      pubs += v
  }
}
