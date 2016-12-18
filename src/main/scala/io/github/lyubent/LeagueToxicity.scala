package io.github.lyubent


import io.github.lyubent.util.FileUtil
import org.apache.spark.sql.SparkSession


object LeagueToxicity {

  def main(args: Array[String]): Unit = {

    val spark = SparkSession.builder()
                            .appName("Toxic_League")
                            .enableHiveSupport()
                            .getOrCreate()

    val baseDF =  spark.read
                       .format("json")
                       .load(FileUtil.getDatasetPath()).cache()

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
}
