package io.github.lyubent.util

import java.util.Properties

/**
 * Created by core on 18/12/2016.
 */
object FileUtil {

  /**
   * Uses classloader to access resource folder and Java config
   * to retrieve the path to the static game dataset.
   *
   * @return Path to static json dataset
   */
  def getDatasetPath(): String = {
    val p = new Properties()
    p.load(getClass.getClassLoader.getResourceAsStream("toxicity.conf"))
    p.getProperty("matches1")
  }

}
