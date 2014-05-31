package com.github.sameersingh.bibere

import scala.collection.mutable.{Buffer, ArrayBuffer, HashMap}

/**
 * @author sameer
 */
case class Affiliation(id: String, name: String, website: Option[String])

case class PersonName(first: String,
                      last: String,
                      preLast: Option[String] = None,
                      lineage: Option[String] = None) {
  def full = first + " " + preLast.fold("")(_ + " ") + last + lineage.fold("")(" " + _)

  def short = first.head + ". " + preLast.fold("")(_.head + ". ") + last + lineage.fold("")(" " + _)
}

object PersonName {
  def apply(name: String): PersonName = {
    val split = name.split("\\s")
    assert(split.length >= 2)
    assert(split.length <= 4)
    PersonName(split.head,
      if (split.length >= 4) split(2) else split.last,
      if (split.length >= 3) Some(split(1)) else None,
      if (split.length >= 4) Some(split.last) else None)
  }
}

case class Author(id: String, name: PersonName, website: Option[String] = None)

object PubType extends Enumeration {
  type PubType = Value
  val Conference, Journal, Workshop, TechReport, Thesis, Report, Chapter, Book, Misc = Value
}

case class Venue(id: String, name: String, acronym: String)

case class Paper(id: String,
                 title: String,
                 pubTypeSlot: String,
                 year: Int,
                 authorIds: Seq[String],
                 venueId: String,
                 abstractText: Option[String] = None,
                 pdfLink: Option[String] = None,
                 pptLink: Option[String] = None,
                 pages: Option[(Int, Int)] = None,
                 extraLinksSlot: Seq[(String, String)] = Seq.empty,
                 extraFieldsSlot: Seq[(String, String)] = Seq.empty,
                 emphasisNote: Option[String] = None,
                 note: Option[String] = None,
                 weight: Option[Double] = None
                  ) {
  def extraLinks: Seq[(String, String)] = if (extraLinksSlot == null) Seq.empty else extraLinksSlot

  def extraFields: Seq[(String, String)] = if (extraFieldsSlot == null) Seq.empty else extraFieldsSlot

  def pubType: PubType.PubType = PubType.withName(pubTypeSlot)
}


class Publications {
  val papers: HashMap[String, Paper] = new HashMap
  val authors: HashMap[String, Author] = new HashMap
  val venues: HashMap[String, Venue] = new HashMap

  def +=(p: Paper): Unit = {
    if (!venues.contains(p.venueId)) println("WARNING: Non-DB venue detected: " + p.venueId)
    if (p.authorIds.exists(a => !authors.contains(a))) println("WARNING: Non-DB author detected")
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

  def papersByYear = papers.values.toSeq.sortBy(_.venueId).sortBy(-_.weight.getOrElse(1.0)).sortBy(_.pubType).sortBy(-_.year)

  def author(id: String) = authors.getOrElse(id, Author(id, PersonName(id)))

  def paper(id: String) = papers(id)

  def venue(id: String) = venues.getOrElse(id, Venue(id, id, id))
}