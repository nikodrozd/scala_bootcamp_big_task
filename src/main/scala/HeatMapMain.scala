import credentials.MongoDBCredentials
import io.db.MongoDriver
import io.file.FileWriter
import utils.HeatMapGenerator

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}

object HeatMapMain extends App {
  val outputFilePath = "src/main/resources/tweets.geojson"
  val qtMaxLevel = 3

  val mongoDriver: MongoDriver = new MongoDriver(MongoDBCredentials.mongoURLWithAuthData,
    MongoDBCredentials.dbName,
    MongoDBCredentials.collectionName)

  val tweetsFuture = mongoDriver.loadAllTweets()

  tweetsFuture.onComplete{
    case Success(tweets) =>
      mongoDriver.closeConnection
      val finalGeoJson = HeatMapGenerator.generateHeatMap(tweets, qtMaxLevel)
      FileWriter.saveData(outputFilePath, finalGeoJson)
    case Failure(exception) =>
      mongoDriver.closeConnection
      throw exception
  }
}
