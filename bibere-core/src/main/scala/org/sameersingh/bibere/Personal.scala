package org.sameersingh.bibere

import java.io.PrintWriter

/**
 * @author sameer
 */
object Personal {

  val directory = "../bibere-examples/personal/"

  val htmlWriter = new BootstrapWriter with PerYear {
    override def mainAuthorId = Some("sameer")

    override def pre(writer: PrintWriter): Unit = {
      writer.println(
        """
          |---
          |layout: main
          |title: "Sameer Singh: Publications"
          |#permalink: /pubs
          |---
          |<h2>Publications</h2>
          |
          |<div>
          |  Publications (grouped by year): <a href="#2016">2016</a>, <a href="#2015">2015</a>, <a href="#2014">2014</a>, <a href="#2013">2013</a>, <a href="#2012">2012</a>, <a href="#2011">2011</a>, <a href="#2010">2010</a>, <a href="#2009">2009</a>, <a href="#2008">2008</a>, <a href="#2007">2007</a>, <a href="#2006">&lt;=2006</a>.
          |  <hr>
          |</div>
        """.stripMargin.trim)
    }

    override def post(writer: PrintWriter): Unit = {}
  }

  val bibtexWriter = new BibtexWriter {
    override def mainAuthorId = Some("sameer")
  }

  def main(args: Array[String]) {
    val pubs = JsonReader.readPublications(directory)
    println("authors: " + pubs.authors.values.mkString("\t"))
    println("venues: " + pubs.venues.values.mkString("\t"))
    println("papers: " + pubs.papersByYear.mkString("\t"))
    htmlWriter.write(pubs, directory + "html/")
    bibtexWriter.write(pubs, directory + "bibtex/")
  }

}
