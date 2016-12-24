package io.github.lyubent

import io.github.lyubent.util.FileUtil
import org.apache.spark.sql.{DataFrame, SparkSession}
import org.apache.spark.storage.StorageLevel


class DynamicMatchQuery(spark: SparkSession) {

  // implicit import for usage of $
  import spark.implicits.StringToColumn

  def runBatch(): Unit = {
    val baseDF = getBaseDF()
    rankedFlex(baseDF)
    rankedSoloQ(baseDF)
  }

  def getBaseDF(): DataFrame = {
    val baseDF =  spark.read
                       .format("json")
                       .load(FileUtil.getConfigProperty("matches_dynamic")).cache()
    baseDF
  }

  def displayQueueTypes(baseDF: DataFrame): Unit = {
    baseDF.select($"queueType").distinct.show()
  }

  def rankedFlex(baseDF: DataFrame): DataFrame = {
    val flexDF = baseDF.select($"matchMode", $"queueType")
                       // DF will be reused for SoloQ
                       .filter($"matchMode" === "CLASSIC").persist(StorageLevel.MEMORY_ONLY)
                       .filter($"queueType" === "RANKED_FLEX_SR")
    flexDF
  }

  def rankedSoloQ(baseDF: DataFrame): DataFrame = {
    val soloQDF = baseDF.select($"matchMode", $"queueType")
                        // DF will be reused for FlexQ
                        .filter($"matchMode" === "CLASSIC").persist(StorageLevel.MEMORY_ONLY)
                        .filter($"queueType" === "TEAM_BUILDER_RANKED_SOLO")
    soloQDF
  }

  def divisionDisparity(): Unit = {
    // find games with large disparity, we want games where:
    // we have gold and bronze
    // we have plat and silver / bronze players
    // diamond and gold / silver / bronze

    // did the higher elo players have a better KDR / higher cs
    // did low elo players feed? (k/d/r)

    // who took down most objectives

    // do higher elo players prefer a specific role
  }
}
