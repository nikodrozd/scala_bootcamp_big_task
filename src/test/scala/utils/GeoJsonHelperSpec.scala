package utils

import model._
import org.scalatest.GivenWhenThen
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import play.api.libs.json.{JsArray, JsObject, Json}

class GeoJsonHelperSpec extends AnyFlatSpec with Matchers with GivenWhenThen{

  "GeoJsonHelper.convertTweetToGeoJson" should "take Tweet object as input and return JsObject with tweet data" in {
    Given("Tweet object and expected JsObject")
    val tweetId = 321L
    val tweetDateTime = "Tue Sep 28 14:48:30 +0000 2021"
    val tweetText = "test_text"
    val userId = 123L
    val userName = "test_user"
    val placeId = "123qwer123"
    val placeType = "poi"
    val coordinateArr = Array(Coordinate(20.0, 10.0), Coordinate(20.0, 10.0), Coordinate(20.0, 10.0), Coordinate(20.0, 10.0))
    val tweet = Tweet(tweetId, tweetDateTime, User(userId, userName), tweetText, Place(placeId, placeType, coordinateArr))
    val expJsObject: JsObject = Json.obj(
      "type" -> "Feature",
      "geometry" -> Json.obj(
        "type" -> "Point",
        "coordinates" -> tweet.place.getPointCoordinateAsSeq),
      "properties" -> Json.obj(
        "user" -> userName,
        "time" -> tweetDateTime,
        "tweet" -> tweetText))

    When("convertTweetToGeoJson function called for given tweet object")
    val resJsObject: JsObject = GeoJsonHelper.convertTweetToGeoJson(tweet)

    Then("result JsObject should be the same as expected one")
    resJsObject should equal(expJsObject)
  }

  "GeoJsonHelper.convertQuadTreeWithColorsToGeoJson" should "take tuple of color string and QuadTreeGeo as input and return JsObject with color and polygon data" in {
    Given("color string, QuadTreeGeo and expected JsObject")
    val color = "#76ff5d"
    val quadTreeGeo = QuadTreeGeo()
    val expJsObject: JsObject = Json.obj(
      "type" -> "Feature",
      "geometry" -> Json.obj(
        "type" -> "Polygon",
        "coordinates" -> Json.arr(
          Json.arr(
            Json.arr(-180,90),
            Json.arr(180,90),
            Json.arr(180,-90),
            Json.arr(-180,-90),
            Json.arr(-180,90)
          )
        )),
      "properties" -> Json.obj(
        "fill" -> color,
        "stroke-width" -> 0))

    When("convertQuadTreeWithColorsToGeoJson is called for given values")
    val resJsObject: JsObject = GeoJsonHelper.convertQuadTreeWithColorsToGeoJson((color, quadTreeGeo))

    Then("result JsObject should be the same as expected one")
    resJsObject should equal(expJsObject)
  }

  "GeoJsonHelper.aggregateGeoJsonNodes" should "take JsArray and JsObject as input and return JsArray with merged content" in {
    Given("JsArray and JsObject, expected JsArray")
    val inputJsArray: JsArray = Json.arr(
      Json.obj(
        "type" -> "Point",
        "coordinate" -> Json.arr(100.0, 20.0)
      ),
      Json.obj(
        "type" -> "Point",
        "coordinate" -> Json.arr(110.0, 30.0)
      ))
    val inputJsObject: JsObject = Json.obj(
      "type" -> "Point",
      "coordinate" -> Json.arr(120.0, 40.0)
    )
    val expJsArray: JsArray = Json.arr(
      Json.obj(
        "type" -> "Point",
        "coordinate" -> Json.arr(100.0, 20.0)
      ),
      Json.obj(
        "type" -> "Point",
        "coordinate" -> Json.arr(110.0, 30.0)
      ),
      Json.obj(
        "type" -> "Point",
        "coordinate" -> Json.arr(120.0, 40.0)
      ))

    When("aggregateGeoJsonNodes is called for given input parameters")
    val resJsArray = GeoJsonHelper.aggregateGeoJsonNodes(inputJsArray, inputJsObject)

    Then("result JsArray should be the same as expected one")
    resJsArray should equal(expJsArray)
  }

  "GeoJsonHelper.aggregateGeoJsonArrays" should "take two JsArrays as input and return JsArray with merged content" in {
    Given("input JsArrays, expected final JsArray")
    val inputJsArrayFirst: JsArray = Json.arr(
      Json.obj(
        "type" -> "Point",
        "coordinate" -> Json.arr(100.0, 20.0)
      ),
      Json.obj(
        "type" -> "Point",
        "coordinate" -> Json.arr(110.0, 30.0)
      ))
    val inputJsArraySecond: JsArray = Json.arr(
      Json.obj(
        "type" -> "Point",
        "coordinate" -> Json.arr(120.0, 40.0)
      ),
      Json.obj(
        "type" -> "Point",
        "coordinate" -> Json.arr(130.0, 50.0)
      ))
    val expJsArray: JsArray = Json.arr(
      Json.obj(
        "type" -> "Point",
        "coordinate" -> Json.arr(100.0, 20.0)
      ),
      Json.obj(
        "type" -> "Point",
        "coordinate" -> Json.arr(110.0, 30.0)
      ),
      Json.obj(
        "type" -> "Point",
        "coordinate" -> Json.arr(120.0, 40.0)
      ),
      Json.obj(
        "type" -> "Point",
        "coordinate" -> Json.arr(130.0, 50.0)
      ))

    When("aggregateGeoJsonArrays is called for given input parameters")
    val resJsArray = GeoJsonHelper.aggregateGeoJsonArrays(inputJsArrayFirst, inputJsArraySecond)

    Then("result JsArray should be the same as expected one")
    resJsArray should equal(expJsArray)
  }

  "GeoJsonHelper.generateResultGeoJson" should "take JsArray as input parameter and return JsObject with content of input JsArray and additional fields" in {
    Given("JsArray and expected JsObject")
    val inputJsArray: JsArray = Json.arr(
      Json.obj(
        "type" -> "Point",
        "coordinate" -> Json.arr(100.0, 20.0)
      ),
      Json.obj(
        "type" -> "Point",
        "coordinate" -> Json.arr(110.0, 30.0)
      ))
    val expJsObject = Json.obj(
      "type" -> "FeatureCollection",
      "features" -> Json.arr(
      Json.obj(
        "type" -> "Point",
        "coordinate" -> Json.arr(100.0, 20.0)
      ),
      Json.obj(
        "type" -> "Point",
        "coordinate" -> Json.arr(110.0, 30.0)
      )))

    When("generateResultGeoJson is called for given input parameters")
    val resJsObject = GeoJsonHelper.generateResultGeoJson(inputJsArray)

    Then("result JsArray should be the same as expected one")
    resJsObject should equal(expJsObject)
  }

}
