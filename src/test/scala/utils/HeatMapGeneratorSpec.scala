package utils

import model._
import org.scalatest.GivenWhenThen
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import play.api.libs.json.Json

class HeatMapGeneratorSpec extends AnyFlatSpec with Matchers with GivenWhenThen {

  "HeatMapGenerator.generatePoiTweetsGeoJson" should "take sequence of tweets as input parameter and return JsArray with content of these tweets" in {
    Given("sequence of tweets and expected JsArray")
    val userNameSeq = Seq("user_name", "user_name2")
    val timeSeq = Seq("27/09/2021", "28/09/2021")
    val textSeq = Seq("some text", "some text2")
    val tweetSeq = Seq(
      Tweet(10L, timeSeq(0), User(20L, userNameSeq(0)), textSeq(0), Place("testId1", "poi", Seq(Coordinate(10.0, 20.0)))),
      Tweet(11L, timeSeq(1), User(21L, userNameSeq(1)), textSeq(1), Place("testId2", "poi", Seq(Coordinate(15.0, 25.0)))))
    val expJsArray = Json.arr(
      Json.obj(
        "type" -> "Feature",
        "geometry" -> Json.obj(
          "type" -> "Point",
          "coordinates" -> Json.arr(10, 20)
        ),
        "properties" -> Json.obj(
          "user" -> userNameSeq(0),
          "time" -> timeSeq(0),
          "tweet" -> textSeq(0)
        )
      ),
      Json.obj(
        "type" -> "Feature",
        "geometry" -> Json.obj(
          "type" -> "Point",
          "coordinates" -> Json.arr(15, 25)
        ),
        "properties" -> Json.obj(
          "user" -> userNameSeq(1),
          "time" -> timeSeq(1),
          "tweet" -> textSeq(1)
        )
      )
    )

    When("generatePoiTweetsGeoJson function is called for given parameters")
    val resJsArray = HeatMapGenerator.generatePoiTweetsGeoJson(tweetSeq)

    Then("result JsArray should be the same as expected one")
    resJsArray should equal(expJsArray)
  }

  "HeatMapGenerator.generateInitialQuadTree" should "take sequence of tweets as input and return generated QuadTreeGeo" in {
    Given("sequence of tweets, quad tree level and expected QuadTreeGeo")
    val userNameSeq = Seq("user_name", "user_name2")
    val timeSeq = Seq("27/09/2021", "28/09/2021")
    val textSeq = Seq("some text", "some text2")
    val tweetSeq = Seq(
      Tweet(10L, timeSeq(0), User(20L, userNameSeq(0)), textSeq(0), Place("testId1", "poi", Seq(Coordinate(-100.0, 20.0)))),
      Tweet(11L, timeSeq(1), User(21L, userNameSeq(1)), textSeq(1), Place("testId2", "poi", Seq(Coordinate(115.0, 25.0)))))
    val expQuadTreeGeo = tweetSeq.foldLeft(QuadTreeGeo())((acc, tweet) => acc.addTweet(tweet))

    When("generateInitialQuadTree function is called with given parameters")
    val resQuadTreeGeo = HeatMapGenerator.generateInitialQuadTree(tweetSeq)

    Then("result QuadTreeGeo should be the same as expected one")
    resQuadTreeGeo.level should equal(expQuadTreeGeo.level)
    resQuadTreeGeo.tweetsInTile should equal(expQuadTreeGeo.tweetsInTile)
    resQuadTreeGeo.center should equal(expQuadTreeGeo.center)
    resQuadTreeGeo.halfHeight should equal(expQuadTreeGeo.halfHeight)
    resQuadTreeGeo.halfWidth should equal(expQuadTreeGeo.halfWidth)
  }

  "HeatMapGenerator.generateQuadTreeGeoJson" should "take quad tree and level of nodes for generation as input and return JsArray with content of these nodes" in {
    Given("quad tree, level of nodes and expected JsArray")
    val levelOfNodes = 0
    val quadTreeGeo = QuadTreeGeo()
    val expJsArray = Json.arr(
      Json.obj(
        "type" -> "Feature",
        "geometry" -> Json.obj(
          "type" -> "Polygon",
          "coordinates" -> Json.arr(
            Json.arr(
              Json.arr(-180, 90),
              Json.arr(180, 90),
              Json.arr(180, -90),
              Json.arr(-180, -90),
              Json.arr(-180, 90)
            )
          )),
        "properties" -> Json.obj(
          "fill" -> "#000000",
          "stroke-width" -> 0))
    )
    When("generateQuadTreeGeoJson function is called for given parameters")
    val resJsArray = HeatMapGenerator.generateQuadTreeGeoJson(quadTreeGeo, levelOfNodes)

    Then("result JsArray should be the same as expected one")
    resJsArray should equal(expJsArray)
  }

  "HeatMapGenerator.hslToHex" should "convert input HSL color value to string in HEX format" in {
    Given("HSL values and expected HEX value")
    val h = 220
    val s = 100
    val l = 50
    val expHex = "#0055ff"

    When("hslToHex function is called for given values")
    val resHex = HeatMapGenerator.hslToHex(h, s, l)

    Then("result HEX string should be same as expected one")
    resHex should equal(expHex)
  }

  "HeatMapGenerator.generateHeatMap" should "take sequence of tweets and quad tree level as input and return JsObject with generated heat map" in {
    Given("quad tree level, sequence of tweets and expected JsObject")
    val level = 0
    val userNameSeq = Seq("user_name", "user_name2")
    val timeSeq = Seq("27/09/2021", "28/09/2021")
    val textSeq = Seq("some text", "some text2")
    val tweetSeq = Seq(
      Tweet(10L, timeSeq(0), User(20L, userNameSeq(0)), textSeq(0), Place("testId1", "poi", Seq(Coordinate(-100.0, 20.0)))),
      Tweet(11L, timeSeq(1), User(21L, userNameSeq(1)), textSeq(1), Place("testId2", "poi", Seq(Coordinate(115.0, 25.0)))))

    val expJsObject = Json.obj(
      "type" -> "FeatureCollection",
      "features" -> Json.arr(
        Json.obj(
          "type" -> "Feature",
          "geometry" -> Json.obj(
            "type" -> "Polygon",
            "coordinates" -> Json.arr(
              Json.arr(
                Json.arr(-180, 90),
                Json.arr(180, 90),
                Json.arr(180, -90),
                Json.arr(-180, -90),
                Json.arr(-180, 90)
              )
            )),
          "properties" -> Json.obj(
            "fill" -> "#803300",
            "stroke-width" -> 0)
        ),
        Json.obj(
          "type" -> "Feature",
          "geometry" -> Json.obj(
            "type" -> "Point",
            "coordinates" -> Json.arr(-100,20)
          ),
          "properties" -> Json.obj(
            "user" -> userNameSeq(0),
            "time" -> timeSeq(0),
            "tweet" -> textSeq(0)
          )
        ),
        Json.obj(
          "type" -> "Feature",
          "geometry" -> Json.obj(
            "type" -> "Point",
            "coordinates" -> Json.arr(115,25)
          ),
          "properties" -> Json.obj(
            "user" -> userNameSeq(1),
            "time" -> timeSeq(1),
            "tweet" -> textSeq(1)
          )
        )
      )
    )
    When("generateQuadTreeGeoJson function is called for given parameters")
    val resJsObject = HeatMapGenerator.generateHeatMap(tweetSeq, level)

    Then("result JsArray should be the same as expected one")
    resJsObject should equal(expJsObject)
  }

}
