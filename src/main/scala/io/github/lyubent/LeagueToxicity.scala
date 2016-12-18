package io.github.lyubent

import java.util.Properties

import org.apache.spark.sql.SparkSession


object LeagueToxicity {

  def main(args: Array[String]): Unit = {

    val spark = SparkSession.builder()
                            .appName("Toxic_League")
                            .enableHiveSupport()
                            .getOrCreate()

    val baseDF =  spark.read
                       .format("json")
                       .load(getDatasetPath()).cache()

    // implicit import for usage of $
    import spark.implicits.StringToColumn

    // View highest achieved tier by looking at participants.element.highestAchievedSeasonTier
    val tierSummary = baseDF.select($"participants".getItem(3)
                                                   .getField("highestAchievedSeasonTier")
                                                   .as("highest_achieved_tier"))
                                                   .groupBy("highest_achieved_tier")
                                                   .count()
                                                   .orderBy($"count".desc)
    println("Tier of all players for all games:\n" + tierSummary.show())
  }

  /**
   * Uses classloader to access resource folder and Java config
   * to retrieve the path to the static game dataset.
   *
   * @return Path to static json dataset
   */
  // TODO Function belongs in utility class, not in entry point.
  private def getDatasetPath(): String = {
    val p = new Properties()
    p.load(getClass.getClassLoader.getResourceAsStream("toxicity.conf"))
    println("dafq??? " + p.getProperty("matches1"))
    p.getProperty("matches1")
  }
}
