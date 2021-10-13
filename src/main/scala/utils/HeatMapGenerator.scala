package utils

import model.{QuadTreeGeo, Tweet}
import play.api.libs.json.{JsArray, JsObject, Json}
import utils.GeoJsonHelper._

object HeatMapGenerator extends java.io.Serializable{

  def generateHeatMap(tweets: Seq[Tweet], qtMaxLevel: Int): JsObject = {
    val poiTweetsGeoJson: JsArray = generatePoiTweetsGeoJson(tweets)
    val initQuadTreeGeo: QuadTreeGeo = generateInitialQuadTree(tweets)
    val qtGeoJson: JsArray = generateQuadTreeGeoJson(initQuadTreeGeo, qtMaxLevel)
    val totalGeoJsonArray = aggregateGeoJsonArrays(qtGeoJson, poiTweetsGeoJson)

    generateResultGeoJson(totalGeoJsonArray)
  }

  private[utils] def generatePoiTweetsGeoJson(tweets: Seq[Tweet]): JsArray = Spark.sc.parallelize(tweets, Spark.parallelizeLevel)
    .map(convertTweetToGeoJson)
    .aggregate(Json.arr())(aggregateGeoJsonNodes, aggregateGeoJsonArrays)

  private[utils] def generateInitialQuadTree(tweets: Seq[Tweet]): QuadTreeGeo =
    tweets.foldLeft(QuadTreeGeo()){
      (qt, tweet) => qt.addTweet(tweet)
    }

  private[utils] def generateQuadTreeGeoJson(quadTreeGeo: QuadTreeGeo, qtMaxLevel: Int): JsArray = {
    val max = quadTreeGeo.getMaxNumberOfPointsOnLevel(qtMaxLevel)

    def assignColorsToQuadTree: QuadTreeGeo => (String, QuadTreeGeo) = (qt: QuadTreeGeo) => {
      val h = 111
      val s = 100
      val lBase = 80
      val lDelta = 70
      val l = lBase - qt.getNumberOfPointInTile/max.toDouble * lDelta
      (hslToHex(h, s, l), qt)
    }

    Spark.sc.parallelize(quadTreeGeo.getNodesFromLevel(qtMaxLevel), Spark.parallelizeLevel).map(assignColorsToQuadTree)
      .map(convertQuadTreeWithColorsToGeoJson)
      .aggregate(Json.arr())(aggregateGeoJsonNodes, aggregateGeoJsonArrays)
  }



  private[utils] def hslToHex(h: Double, s: Double, l: Double): String = {
    val lFractal = l / 100
    val a = s * Math.min(lFractal, 1 - lFractal) / 100

    def f(n: Double): String = {
      val k = (n + h / 30) % 12
      val color = lFractal - a * Math.max(Math.min(Math.min(k - 3, 9 - k), 1), -1)
      Math.round(255 * color).toHexString.padTo[Char, String](2, '0')
    }

    s"#${f(0)}${f(8)}${f(4)}"
  }

}