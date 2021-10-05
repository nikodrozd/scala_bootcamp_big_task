package utils

import org.apache.spark.{SparkConf, SparkContext}

object Spark {
  val appName: String = "HeatMap Generator"
  val master: String = "local[*]"
  val conf: SparkConf = new SparkConf().setAppName(appName).setMaster(master)
  val sc = new SparkContext(conf)
  val parallelizeLevel = 128
}
