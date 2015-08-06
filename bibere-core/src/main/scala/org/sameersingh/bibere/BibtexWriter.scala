package org.sameersingh.bibere

import java.io.{PrintWriter, FileOutputStream, OutputStreamWriter}

/**
 * @author sameer
 */
class BibtexWriter extends Writer {

  def filename = "papers.bib"

  def write(pubs: Publications, path: String) {
    val file = new OutputStreamWriter(
      new FileOutputStream(path + "/" + filename), "UTF-8")
    val writer = new PrintWriter(file)
    for (p <- pubs.papersByYear) {
      writer.println(bibtex(p, pubs))
      writer.flush()
    }
    writer.close()
  }

  def bibtex(p: Paper, pubs: Publications): String = {
    val sb = new StringBuffer
    val vt = BibtexHelper.entryType(p.pubType)
    sb append ("@" + vt + "{")
    sb.append(p.id + ",\n")
    sb.append(" %s = {%s},\n" format("author", p.authorIds.map(pubs author _)
          .map(a => if(mainAuthorId == Some(a.id)) "\\textbf{" + a.name.full + "}" else a.name.full)
          .mkString(" and ")))
    sb.append(" %s = {%s},\n" format("title", p.title))
    sb.append(" %s = {%s},\n" format(BibtexHelper.venueTitle(p.pubType), pubs.venue(p.venueId).name))
    sb.append(" %s = {%d}\n" format("year", p.year))
    sb append "}"
    sb.toString
  }
}

object BibtexWriter extends BibtexWriter

object BibtexHelper {

  import PubType._

  def entryType(v: Value): String = v match {
    case Conference => "inproceedings"
    case Workshop => "inproceedings"
    case Demo => "inproceedings"
    case TechReport => "techreport"
    case Patent => "techreport"
    case Thesis => "phdthesis"
    case _ => "misc"
  }

  def venueTitle(v: Value): String = v match {
    case Conference => "booktitle"
    case Workshop => "booktitle"
    case Demo => "booktitle"
    case TechReport => "institution"
    case Patent => "institution"
    case Thesis => "school"
    case _ => "series"
  }
}