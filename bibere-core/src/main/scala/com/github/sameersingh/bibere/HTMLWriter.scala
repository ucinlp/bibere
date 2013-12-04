package com.github.sameersingh.bibere

import java.io.{PrintWriter, FileOutputStream, OutputStreamWriter}

/**
 * @author sameer
 */
class HTMLWriter extends Writer {
  val filename = "index.html"

  def pageTitle = "Publications"

  def papersClass = "papers"

  def mainAuthorClass = "mainAuthor"

  def authorClass = "author"

  def abstractClass = "abstract"

  def titleClass = "title"

  def venueClass = "venue"

  def yearClass = "year"

  def linkClass = "link"

  def write(pubs: Publications, path: String) {
    val file = new OutputStreamWriter(
      new FileOutputStream(path + "/" + filename), "UTF-8")
    val writer = new PrintWriter(file)
    pre(writer)
    writePubs(pubs, writer)
    post(writer)
    writer.flush()
    writer.close()
  }

  def writePubs(pubs: Publications, writer: PrintWriter) = {
    writer.println("<ul class=\"" + papersClass + "\">")
    for (p <- pubs.papersByYear) pub(p, writer, pubs)
    writer.println("</ul>")
  }

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

  def post(writer: PrintWriter, p: Paper, pubs: Publications) {
    p.abstractText.foreach(a => abstractText(writer, a))
  }

  def pub(p: Paper, writer: PrintWriter, pubs: Publications) {
    writer.println()
    writer.println("<li>")
    for (aid <- p.authorIds) {
      pubs.authors.get(aid).fold({
        author(writer, Author(aid, PersonName(aid)))
      })(a => {author(writer, a)})
      if (aid == p.authorIds.last) writer.print(". ") else writer.print(", ")
    }
    title(writer, p.title)
    pubs.venues.get(p.venueId).fold({
      venue(writer, Venue(p.venueId, p.venueId, p.venueId))
    })(p => {venue(writer, p)})
    year(writer, p.year)
    links(writer, p)
    post(writer, p, pubs)
    writer.println("</li>")
    writer.println()
  }

  def author(writer: PrintWriter, a: Author) {
    val authClass = mainAuthorId.fold(authorClass)(m => if (a.id == m) mainAuthorClass else authorClass)
    writer.print("<span class=\"%s\">" format (authClass))
    a.website.foreach(w => writer.print("<a href=\"%s\">" format (w)))
    writer.print(a.name.short)
    a.website.foreach(w => writer.print("</a>"))
    writer.print("</span>")
  }

  def title(writer: PrintWriter, t: String) {
    writer.print("<span class=\"%s\">" format (titleClass))
    writer.print(t)
    writer.print(". ")
    writer.print("</span>")
  }

  def venue(writer: PrintWriter, v: Venue) {
    writer.print("<span class=\"%s\">" format venueClass)
    writer.print(v.name)
    writer.print(". ")
    writer.print("</span>")
  }

  def year(writer: PrintWriter, y: Int) {
    writer.print("<span class=\"%s\">" format (yearClass))
    writer.print(y)
    writer.println("</span>")
  }

  def abstractText(writer: PrintWriter, abs: String) {
    writer.print("<span class=\"%s\">" format (abstractClass))
    writer.print(abs)
    writer.println("</span>")
  }

  def links(writer: PrintWriter, p: Paper) {
    if(p.pdfLink.isDefined || p.pptLink.isDefined || p.extraLinks.length > 0) {
      p.pdfLink.foreach(l => {
        writer.println("<br/>")
        writer.print("<span class=\"%s\">" format (linkClass))
        writer.print("<a href=\"%s\">" format (l))
        writer.print("PDF")
        writer.print("</a>")
        writer.println("</span>")
      })
      p.pptLink.foreach(l => {
        writer.print("<span class=\"%s\">" format (linkClass))
        writer.print("<a href=\"%s\">" format (l))
        writer.print("PPT")
        writer.print("</a>")
        writer.println("</span>")
      })
      p.extraLinks.foreach(n_l => {
        writer.print("<span class=\"%s\">" format (linkClass))
        writer.print("<a href=\"%s\">" format (n_l._2))
        writer.print(n_l._1)
        writer.print("</a>")
        writer.println("</span>")
      })

    }
  }
}

object HTMLWriter extends HTMLWriter

trait PerYear extends HTMLWriter {
  def yearGroupClass = "yearGroup"

  override def writePubs(pubs: Publications, writer: PrintWriter) = {
    val grouped = pubs.papersByYear.groupBy(_.year).toSeq.sortBy(-_._1)
    for ((y,ps) <- grouped) {
      writer.println()
      writer.println("<span class=\"%s\">%d</span>" format (yearGroupClass, y))
      writer.println("<ul class=\"" + papersClass + "\">")
      for(p <- ps) pub(p, writer, pubs)
      writer.println("</ul>")
      writer.println()
    }
  }
}

class BootstrapWriter extends HTMLWriter {
  override def links(writer: PrintWriter, p: Paper) = {
    if(p.pdfLink.isDefined || p.pptLink.isDefined || p.extraLinks.length > 0) {
      writer.println("<div class=\"btn-group\">")
      writer.println("<button type=\"button\" class=\"btn btn-default dropdown-toggle\" data-toggle=\"dropdown\">")
      writer.println("Download <span class=\"caret\"></span>")
      writer.println("</button>")
      writer.println("<ul class=\"dropdown-menu\" role=\"menu\">")
      val links = p.pdfLink.map("Paper (PDF)" -> _) ++ p.pptLink.map("Presentation" -> _) ++ p.extraFields
      for((n,l) <- links) {
        writer.println("<li><a href=\"%s\">%s</a></li>" format(l, n))
      }
      writer.println("</ul></div>")
    }
  }

  override def post(writer: PrintWriter, p: Paper, pubs: Publications) = {
    val id = p.id.replaceAll("[^0-9a-zA-Z]", "_")
    val abs = p.abstractText.getOrElse("Coming Soon!")
    val bib = BibtexWriter.bibtex(p, pubs)
    writer.print("""
        <div class="btn-group" data-toggle="buttons">
          <label class="btn btn-primary details-btn" data-toggle="collapse" data-target="#%s_abstract">
            <input type="checkbox" /> Abstract
          </label>
          <label class="btn btn-info details-btn" data-toggle="collapse" data-target="#%s_bibtex">
            <input type="checkbox" /> BibTex
          </label>
        </div>
        <div class="collapse" id="%s_abstract">
          <div class="panel panel-primary">
            <div class="panel-body details-panel">
              %s
            </div>
          </div>
        </div>
        <div class="collapse" id="%s_bibtex">
          <div class="panel panel-info">
            <pre class="panel-body details-panel">%s</pre>
          </div>
        </div>
    """ format(id, id, id, abs, id, bib))
  }
}