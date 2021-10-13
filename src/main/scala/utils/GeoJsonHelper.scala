package utils

import model.{QuadTreeGeo, Tweet}
import play.api.libs.json.{JsArray, JsObject, Json}

object GeoJsonHelper {

  val convertTweetToGeoJson: Tweet => JsObject = (tweet: Tweet) => {
    Json.obj(
      "type" -> "Feature",
      "geometry" -> Json.obj(
        "type" -> "Point",
        "coordinates" -> tweet.place.getPointCoordinateAsSeq
      ),
      "properties" -> Json.obj(
        "user" -> tweet.user.name,
        "time" -> tweet.dateTime,
        "tweet" -> tweet.text
      )
    )
  }

  val convertQuadTreeWithColorsToGeoJson: ((String, QuadTreeGeo)) => JsObject = (colorWithQt: (String, QuadTreeGeo)) => {
    val color = colorWithQt._1
    val qt = colorWithQt._2
    Json.obj(
      "type" -> "Feature",
      "geometry" -> Json.obj(
        "type" -> "Polygon",
        "coordinates" -> Json.arr(
          Json.arr(
            Json.arr(qt.center.x - qt.halfWidth, qt.center.y + qt.halfHeight),
            Json.arr(qt.center.x + qt.halfWidth, qt.center.y + qt.halfHeight),
            Json.arr(qt.center.x + qt.halfWidth, qt.center.y - qt.halfHeight),
            Json.arr(qt.center.x - qt.halfWidth, qt.center.y - qt.halfHeight),
            Json.arr(qt.center.x - qt.halfWidth, qt.center.y + qt.halfHeight)
          )
        )
      ),
      "properties" -> Json.obj(
        "fill" -> color,
        "stroke-width" -> 0
      )
    )
  }

  val aggregateGeoJsonNodes: (JsArray, JsObject) => JsArray = (arr: JsArray, point: JsObject) => {
    arr.append(point)
  }

  val aggregateGeoJsonArrays: (JsArray, JsArray) => JsArray = (arrX: JsArray, arrY: JsArray) => {
    arrX.++(arrY)
  }

  def generateResultGeoJson(arr: JsArray): JsObject = {
    Json.obj(
      "type" -> "FeatureCollection",
      "features" -> arr
    )
  }

}