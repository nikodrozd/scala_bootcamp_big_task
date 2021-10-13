package utils

import com.typesafe.config.ConfigFactory
import credentials.MongoDBCredentials
import org.apache.spark.SparkContext
import org.apache.spark.sql.SparkSession

object Spark {
  val appName: String = ConfigFactory.load().getString("spark.appName")
  val master: String = ConfigFactory.load().getString("spark.master")
  val sparkSession: SparkSession = SparkSession.builder()
    .master(master)
    .appName(appName)
    .config("spark.mongodb.input.uri", MongoDBCredentials.mongoURLWithAuthDataAndCollection)
    .getOrCreate()
  val sc: SparkContext = sparkSession.sparkContext
  val parallelizeLevel: Int = ConfigFactory.load().getInt("spark.parallelizeLevel")
}
