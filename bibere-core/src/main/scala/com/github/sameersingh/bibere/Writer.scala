package com.github.sameersingh.bibere

import java.io.{FileOutputStream, OutputStreamWriter, PrintWriter}
import java.util.zip.GZIPOutputStream
import scalax.io.Resource

/**
 * @author sameer
 */
trait Writer {
  def mainAuthorId: Option[String] = None

  def write(pubs: Publications, path: String): Unit
}

object JsonWriter extends Writer {
  val authorsFile = "authors.json"
  val venuesFile = "venues.json"
  val papersFile = "papers.json"

  def write(pubs: Publications, path: String) {
    writeAuthors(pubs, path)
    writePapers(pubs, path)
    writeVenues(pubs, path)
  }

  def writeAuthors(pubs: Publications, path: String) {
    val writer = new PrintWriter(new OutputStreamWriter(
      new FileOutputStream(path + "/" + authorsFile), "UTF-8"))
    writer.println(JacksonWrapper.serializePretty(pubs.authors.values.toSeq))
    writer.flush()
    writer.close
  }

  def writePapers(pubs: Publications, path: String) {
    val writer = new PrintWriter(new OutputStreamWriter(
      new FileOutputStream(path + "/" + papersFile), "UTF-8"))
    writer.println(JacksonWrapper.serializePretty(pubs.papersByYear.toSeq))
    writer.flush()
    writer.close
  }

  def writeVenues(pubs: Publications, path: String) {
    val writer = new PrintWriter(new OutputStreamWriter(
      new FileOutputStream(path + "/" + venuesFile), "UTF-8"))
    writer.println(JacksonWrapper.serializePretty(pubs.venues.values.toSeq))
    writer.flush()
    writer.close
  }
}
