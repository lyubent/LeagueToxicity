package io.github.lyubent

import io.github.lyubent.util.FileUtil
import org.apache.spark.sql.SparkSession

class StaticGame {

  def runBatch(spark: SparkSession): Unit = {
    val baseDF =  spark.read
                       .format("json")
                       .load(FileUtil.getConfigProperty("matches1")).cache()

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

    // what tier of players won most games?
    val winnersByDivisionDF = baseDF.select($"participants".getItem(3).getField("highestAchievedSeasonTier").as("highest_achieved_tier"),
                                            $"teams".getItem(0).getField("winner").as("Winner"))
                                    .filter($"Winner")
                                    .groupBy("highest_achieved_tier")
                                    .count()
                                    .withColumnRenamed("count", "win_count")

    // highest percentage win based on total players?
    winnersByDivisionDF.join(tierSummary, tierSummary("highest_achieved_tier") === winnersByDivisionDF("highest_achieved_tier"))
                       .select(tierSummary("highest_achieved_tier"), ($"win_count" / $"count").as("win_ratio"))
                       .orderBy($"win_ratio".desc)
                       .show()

    // was there disparity between match making, lets find games where silver was matched with plat etc.
    // dataset isn't ritch enough to do this analysis. Will have to gather data using Riot API.

    // did the higher tier players carry the games?
    // we cant really tell, although we can get higher kdrs, there is no way of comparing that
    // between divisions as the dataset doesn't show whether plat players were matched with diamond etc.
    baseDF.select($"participants".getItem(3).getField("highestAchievedSeasonTier").as("highest_achieved_tier"),
                  $"participants".getItem(3).getField("stats").getField("champLevel").as("level"),
                  $"participants".getItem(3).getField("stats").getField("goldEarned").as("gold"),
                  $"participants".getItem(3).getField("stats").getField("kills").as("kills"),
                  $"participants".getItem(3).getField("stats").getField("assists").as("assists"),
                  $"participants".getItem(3).getField("stats").getField("deaths").as("deaths"))
          .orderBy($"gold".desc)
          .show()
  }
}
