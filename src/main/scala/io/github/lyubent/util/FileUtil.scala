package io.github.lyubent.util

import java.util.Properties

import scala.tools.nsc.io.{File, Path}

object FileUtil {

  /**
   * Uses classloader to access resource folder and Java config
   * to retrieve the path to the static game dataset.
   *
   * @return Path to static json dataset
   */
  def getConfigProperty(property: String): String = {
    val p = new Properties()
    p.load(getClass.getClassLoader.getResourceAsStream("toxicity.properties"))
    p.getProperty(property)
  }

  /**
   * Appends content to file, checks if exists, if not creates it.
   *
   * @param body Content to be appended to file.
   * @param path String path to the file to save to.
   */
  def appendToFile(body: String, path: String): Unit = {
    val filePath = path
    if (!File(filePath).exists)
      Path(filePath).createFile()
    File(filePath).appendAll(body)
  }
}
