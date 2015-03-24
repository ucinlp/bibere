package com.github.sameersingh.bibere

/**
 * @author sameer
 * @since 3/24/15.
 */
object FileUtil {

  def readFile(file: String): String = {
    val s = io.Source.fromFile(file, "UTF-8")
    val str = s.getLines().mkString("\n")
    s.close()
    str
  }
}
