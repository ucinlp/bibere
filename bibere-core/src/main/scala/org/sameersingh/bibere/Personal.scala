package org.sameersingh.bibere

/**
 * @author sameer
 */
object Personal {

  val directory = "../bibere-examples/personal/"

  val htmlWriter = new BootstrapWriter with PerYear {
    override def mainAuthorId = Some("sameer")
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
