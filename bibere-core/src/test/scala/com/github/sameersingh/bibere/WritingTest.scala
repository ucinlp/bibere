package com.github.sameersingh.bibere

import org.junit.Test
import org.junit.Assert._
import com.google.common.io.Files

/**
 * @author sameer
 */
class WritingTest {

  def synthPubs: Publications = {
    val result = new Publications
    // authors
    result += Author("sameer", PersonName("Sameer", "Singh"), Some("http://www.cs.umass.edu/~sameer"))
    result += Author("andrew", PersonName("Andrew", "McCallum", Some("K.")), None)
    // venues
    result += Venue("icml", "International Conference on Machine Learning (ICML)", "ICML")
    result += Venue("biglearn", "NIPS Workshop on Big Learning", "BigLearning")
    // papers
    result += Paper("biglearn11", "Towards Asynchronous Distributed MCMC Inference for Large Graphical Models",
      PubType.Workshop.toString, 2011, Seq("sameer", "andrew"), "biglearn", Some("abstract text"), Some("pdf-link"),
      Some("ppt-link"), Some((10,100)), Seq(("project", "project-link")), Seq(("tr-num", "UMASS-CS-2013")), Some("Emphasis!"), Some("nothing to see here..."))

    result
  }

  @Test
  def testCaseClassWriting() {
    val testDir = Files.createTempDir().toString
    println(testDir)
    JsonWriter.write(synthPubs, testDir)
    val pubs = JsonReader.readPublications(testDir)
    println("authors: " + pubs.authors.values)
    println("venues: " + pubs.venues.values)
    println("papers: " + pubs.papers.values)
  }

  @Test
  def testHTMLWriting() {
    val testDir = Files.createTempDir().toString
    println(testDir)
    HTMLWriter.write(synthPubs, testDir)
  }

  @Test
  def testBibtexWriting() {
    val testDir = Files.createTempDir().toString
    println(testDir)
    BibtexWriter.write(synthPubs, testDir)
  }
}
