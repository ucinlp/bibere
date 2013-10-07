package com.github.sameersingh.bibere

import scala.collection.mutable.{Buffer, ArrayBuffer, HashMap}

/**
 * @author sameer
 */
case class Affiliation(id: String, name: String, website: Option[String])

case class PersonName(first: String,
                      last: String,
                      preLast: Option[String] = None,
                      lineage: Option[String] = None)

case class Author(id: String, name: PersonName, website: Option[String] = None)

object PubType extends Enumeration {
  type PubType = Value
  val Conference, Journal, Workshop, TechReport, Thesis, Report, Chapter, Book, Misc = Value
}

case class Venue(id: String, name: String, acronym: String)

case class Paper(id: String,
                 title: String,
                 pubType: PubType.PubType,
                 year: Int,
                 authorIds: Seq[String],
                 venueId: String,
                 abstractText: Option[String] = None,
                 pdfLink: Option[String] = None,
                 pptLink: Option[String] = None,
                 pages: Option[(Int, Int)] = None,
                 extraLinks: Seq[(String, String)] = Seq.empty,
                 extraFields: Seq[(String, String)] = Seq.empty)


class Publications {
  val papers: HashMap[String, Paper] = new HashMap
  val authors: HashMap[String, Author] = new HashMap
  val venues: HashMap[String, Venue] = new HashMap

  def +=(p: Paper): Unit = {
    assert(venues.contains(p.venueId))
    assert(p.authorIds.forall(a => authors.contains(a)))
    assert(!papers.contains(p.id))
    papers(p.id) = p
  }

  def +=(v: Venue): Unit = {
    assert(!venues.contains(v.id))
    venues(v.id) = v
  }

  def +=(a: Author): Unit = {
    assert(!authors.contains(a.id))
    authors(a.id) = a
  }

  def author(id: String) = authors(id)

  def paper(id: String) = papers(id)

  def venue(id: String) = venues(id)
}