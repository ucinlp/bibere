package com.github.sameersingh.bibere

import java.io.FileInputStream
import scalax.io.Codec


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
    val reader = scalax.io.Resource.fromInputStream(new FileInputStream(path + "/" + papersFile))
    for(p <- JacksonWrapper.deserialize[Seq[Paper]](reader.reader(Codec.UTF8).lines().foldLeft("")(_ + _)))
      pubs += p
  }

  def readAuthors(pubs: Publications, path: String) {
    val reader = scalax.io.Resource.fromInputStream(new FileInputStream(path + "/" + authorsFile))
    for(a <- JacksonWrapper.deserialize[Seq[Author]](reader.reader(Codec.UTF8).lines().foldLeft("")(_ + _)))
      pubs += a
  }

  def readVenues(pubs: Publications, path: String) {
    val reader = scalax.io.Resource.fromInputStream(new FileInputStream(path + "/" + venuesFile))
    for(v <- JacksonWrapper.deserialize[Seq[Venue]](reader.reader(Codec.UTF8).lines().foldLeft("")(_ + _)))
      pubs += v
  }
}
