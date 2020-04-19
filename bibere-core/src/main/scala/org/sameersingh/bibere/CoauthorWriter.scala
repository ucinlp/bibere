package org.sameersingh.bibere

import java.io.{PrintWriter, FileOutputStream, OutputStreamWriter}

/**
 * @author sameer
 */
trait CoauthorWriter {
  def mainAuthorId: Option[String] = None

  def sortBy: String = "last" // "first", "last", "numPapers", "recent"

  def write(pubs: Publications, path: String): Unit

  def dedup(pubs: Publications): Iterable[Author] = {
    val auths = pubs.paperAuthorIds
      .filterNot(aid => mainAuthorId.map(_ == aid).getOrElse(false))
      .filter(aid => pubs.authors.contains(aid))
      .map(aid => pubs.authors(aid)).toSeq
    sortBy match {
      case "last" => auths.sortBy(_.name.last)
      case "first" => auths.sortBy(_.name.first)
      case "numPapers" => auths.sortBy(a => -pubs.papers.values.filter(p => p.authorIds.contains(a.id)).size)
      case "recent" => auths.sortBy(a => -pubs.papers.values.filter(p => p.authorIds.contains(a.id)).map(_.year).max)
      case _ => auths
    }
  }

}

class HTMLCoauthorWriter extends CoauthorWriter {

  def filename = "index.html"

  def pageTitle = "Coauthors"

  def listClass = ""

  def listItemClass = ""

  def pre(writer: PrintWriter) {
    writer.println("<!DOCTYPE html>")
    writer.println("<html>")
    writer.println("<head>")
    writer.println("<title>")
    writer.println(pageTitle)
    writer.println("</title>")
    writer.println("</head>")
    writer.println("<body>")
  }

  def post(writer: PrintWriter) {
    writer.println("</body>")
    writer.println("</html>")
  }

  def writeCoauthors(authors: Iterable[Author], writer: PrintWriter) {
    writer.println("<ul class=\"%s\">".format(listClass))
    for (a <- authors) {
      writer.println("<li class=\"%s\">".format(listItemClass))
      HTMLWriter.author(writer, a, fullName = true)
      writer.println("</li>\n")
    }
    writer.println("</ul>")
  }

  def write(pubs: Publications, path: String) {
    val file = new OutputStreamWriter(
       new FileOutputStream(path + "/" + filename), "UTF-8")
    val writer = new PrintWriter(file)
    pre(writer)
    val auths = dedup(pubs)
    writeCoauthors(auths, writer)
    post(writer)
    writer.flush()
    writer.close()
  }

}

object HTMLCoauthorWriter extends HTMLCoauthorWriter
