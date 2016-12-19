package io.github.lyubent.util

import java.util.Properties

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
}
