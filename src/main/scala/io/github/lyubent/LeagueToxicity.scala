package io.github.lyubent

import org.apache.spark.sql.SparkSession

object LeagueToxicity {

  def main(args: Array[String]): Unit = {

    val spark = SparkSession.builder()
                            .appName("Toxic_League")
                            .enableHiveSupport()
                            .getOrCreate()

    // run static query analytics
    // new StaticGame().runBatch(spark)

    // run dynamic match query analytics
    new DynamicMatchQuery(spark).runBatch()
  }
}
