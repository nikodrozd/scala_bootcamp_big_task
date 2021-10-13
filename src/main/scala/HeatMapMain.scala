import com.mongodb.spark._
import com.typesafe.config.ConfigFactory
import io.file.FileWriter
import model.Tweet
import play.api.libs.json.JsObject
import utils.Spark.sparkSession.implicits._
import utils.{HeatMapGenerator, Spark}

object HeatMapMain extends App {
  val outputFilePath: String = ConfigFactory.load().getString("common.outputFilePath")
  val qtMaxLevel: Int = ConfigFactory.load().getInt("common.quadTreeMaxLevel")

  val tweets: Seq[Tweet] = MongoSpark.load[Tweet](Spark.sparkSession).as[Tweet].rdd.collect()
  val finalGeoJson: JsObject = HeatMapGenerator.generateHeatMap(tweets, qtMaxLevel)
  FileWriter.saveData(outputFilePath, finalGeoJson)
}