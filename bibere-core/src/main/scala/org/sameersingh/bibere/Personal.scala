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
          |  Publications (grouped by year): <a href="#2018">2018</a>, <a href="#2017">2017</a>, <a href="#2016">2016</a>, <a href="#2015">2015</a>, <a href="#2014">2014</a>, <a href="#2013">2013</a>, <a href="#2012">2012</a>, <a href="#2011">2011</a>, <a href="#2010">2010</a>, <a href="#2009">2009</a>, <a href="#2008">2008</a>, <a href="#2007">2007</a>, <a href="#2006">&lt;=2006</a>.
          |  <hr>
          |</div>
        """.stripMargin.trim)
    }

    override def post(writer: PrintWriter): Unit = {}
  }

  val personalBibtexWriter = new BibtexWriter {
    override def filename: String = "personal.bib"

    override def mainAuthorId = Some("sameer")
  }

  val generalBibtexWriter = new BibtexWriter {
    override def mainAuthorId = None
  }

  val coauthorHTMLWriter = new HTMLCoauthorWriter {
    override def filename: String = "coauthors.html"

    override def mainAuthorId = Some("sameer")

    override def pre(writer: PrintWriter): Unit = {
      writer.println(
        """
          |---
          |layout: main
          |title: "Sameer Singh: Coauthors"
          |#permalink: /coauthors
          |---
          |<h2>Coauthors</h2>
          |
        """.stripMargin.trim)
    }

    override def post(writer: PrintWriter): Unit = {}

    override def sortBy = "numPapers"
}

  def main(args: Array[String]) {
    val pubs = JsonReader.readPublications(directory)
    println("authors: " + pubs.authors.values.mkString("\t"))
    println("venues: " + pubs.venues.values.mkString("\t"))
    println("papers: " + pubs.papersByYear.mkString("\t"))
    htmlWriter.write(pubs, directory + "html/")
    coauthorHTMLWriter.write(pubs, directory + "html/")
    personalBibtexWriter.write(pubs, directory + "bibtex/")
    generalBibtexWriter.write(pubs, directory + "bibtex/")
  }

}
