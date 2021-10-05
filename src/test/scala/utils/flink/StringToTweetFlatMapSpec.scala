package utils.flink

import model.{Coordinate, Place, Tweet, User}
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.{JsonNode, ObjectMapper}
import org.apache.flink.util.Collector
import org.scalamock.scalatest.MockFactory
import org.scalatest.GivenWhenThen
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class StringToTweetFlatMapSpec extends AnyFlatSpec with Matchers with GivenWhenThen with MockFactory{

  "StringToTweetFlatMap.jsonNodeToCoordinate" should "take json node as input and return Coordinate object" in {
    Given("StringToTweetFlatMap object, json node value and expected result coordinate")
    val parser: ObjectMapper = new ObjectMapper()
    val json: String = "[20.0, 10.0]"
    val jsonCoordinate: JsonNode = parser.readValue(json, classOf[JsonNode])
    val expCoordinate = Coordinate(20.0, 10.0)

    When("jsonNodeToCoordinate function is called for given json node")
    val resCoordinate = StringToTweetFlatMap().jsonNodeToCoordinate(jsonCoordinate)

    Then("result Coordinate should be the same as expected one")
    resCoordinate should equal(expCoordinate)
  }

  "StringToTweetFlatMap.parseCoordinates" should "take Option[JsonNode] as input parameter and return array of coordinates" in {
    Given("StringToTweetFlatMap object, json node in Option and expected result coordinate")
    val parser: ObjectMapper = new ObjectMapper()
    val json: String = "[[[20.0, 10.0], [25.0, 15.0], [30.0, 20.0], [35.0, 25.0]]]"
    val jsonCoordinatesOpt = Some(parser.readValue(json, classOf[JsonNode]))
    val expCoordinateArr = Array(Coordinate(20.0, 10.0), Coordinate(25.0, 15.0), Coordinate(30.0, 20.0), Coordinate(35.0, 25.0))

    When("parseCoordinates function is called for given json node")
    val resArr = StringToTweetFlatMap().parseCoordinates(jsonCoordinatesOpt)

    Then("result array should be the same as expected one")
    resArr should equal(expCoordinateArr)
  }

  it should "take None as input parameter and return empty array" in {
    Given("StringToTweetFlatMap object, json node in Option as None")
    val jsonCoordinatesOpt = None
    val expCoordinateArr = Array.empty

    When("parseCoordinates function is called for given json node")
    val resArr = StringToTweetFlatMap().parseCoordinates(jsonCoordinatesOpt)

    Then("result array should be empty")
    resArr should equal(expCoordinateArr)
  }

  "StringToTweetFlatMap.parsePlace" should "take JsonNode as input parameter and return Place object" in {
    Given("StringToTweetFlatMap object, json node and expected result place")
    val parser: ObjectMapper = new ObjectMapper()
    val id = "123qwer123"
    val place_type = "poi"
    val coordinateArr = Array(Coordinate(20.0, 10.0), Coordinate(25.0, 15.0), Coordinate(30.0, 20.0), Coordinate(35.0, 25.0))
    val json: String = s"""{"id": "$id",
                       "place_type": "$place_type",
                       "bounding_box": {
                       "coordinates": [[[20.0, 10.0], [25.0, 15.0], [30.0, 20.0], [35.0, 25.0]]]}}"""
    val jsonPlace = parser.readValue(json, classOf[JsonNode])
    val expPlace = Place(id, place_type, coordinateArr)

    When("parsePlace function is called for given json node")
    val resPlace = StringToTweetFlatMap().parsePlace(jsonPlace)

    Then("result place should be the same as expected one")
    resPlace should equal(expPlace)
  }

  "StringToTweetFlatMap.parseUser" should "take JsonNode as input parameter and return User object" in {
    Given("StringToTweetFlatMap object, json node and expected result user")
    val parser: ObjectMapper = new ObjectMapper()
    val id = 123L
    val name = "test_user"
    val json: String = s"""{"id": $id,
                       "name": "$name"}"""
    val jsonUser = parser.readValue(json, classOf[JsonNode])
    val expUser = User(id, name)

    When("parseUser function is called for given json node")
    val resPlace = StringToTweetFlatMap().parseUser(jsonUser)

    Then("result user should be the same as expected one")
    resPlace should equal(expUser)
  }

  "StringToTweetFlatMap.parseTweet" should "take JsonNode as input parameter and return Tweet object" in {
    Given("StringToTweetFlatMap object, json node and expected result tweet")
    val parser: ObjectMapper = new ObjectMapper()
    val tweetId = 321L
    val tweetDateTime = "Tue Sep 28 14:48:30 +0000 2021"
    val tweetText = "test_text"
    val userId = 123L
    val userName = "test_user"
    val placeId = "123qwer123"
    val placeType = "poi"
    val coordinateArr = Array(Coordinate(20.0, 10.0), Coordinate(25.0, 15.0), Coordinate(30.0, 20.0), Coordinate(35.0, 25.0))
    val json: String = s"""{"id": $tweetId,
                       "created_at": "$tweetDateTime",
                       "text": "$tweetText",
                       "user": {"id": $userId,
                                "name": "$userName"},
                       "place": {"id": "$placeId",
                                 "place_type": "$placeType",
                                 "bounding_box": {
                                 "coordinates": [[[20.0, 10.0], [25.0, 15.0], [30.0, 20.0], [35.0, 25.0]]]}}}"""
    val jsonTweet = parser.readValue(json, classOf[JsonNode])
    val expTweet = Tweet(tweetId, tweetDateTime, User(userId, userName), tweetText, Place(placeId, placeType, coordinateArr))

    When("parsePlace function is called for given json node")
    val resTweet = StringToTweetFlatMap().parseTweet(jsonTweet)

    Then("result place should be the same as expected one")
    resTweet should equal(expTweet)
  }

  "StringToTweetFlatMap.flatMap" should "transform json string to tweet object" in {
    Given("json string, expected Tweet object and mocked collector")
    val tweetId = 321L
    val tweetDateTime = "Tue Sep 28 14:48:30 +0000 2021"
    val tweetText = "test_text"
    val userId = 123L
    val userName = "test_user"
    val placeId = "123qwer123"
    val placeType = "poi"
    val coordinateArr = Array(Coordinate(20.0, 10.0), Coordinate(25.0, 15.0), Coordinate(30.0, 20.0), Coordinate(35.0, 25.0))
    val json: String = s"""{"id": $tweetId,
                       "created_at": "$tweetDateTime",
                       "text": "$tweetText",
                       "user": {"id": $userId,
                                "name": "$userName"},
                       "place": {"id": "$placeId",
                                 "place_type": "$placeType",
                                 "bounding_box": {
                                 "coordinates": [[[20.0, 10.0], [25.0, 15.0], [30.0, 20.0], [35.0, 25.0]]]}}}"""
    val expTweet = Tweet(tweetId, tweetDateTime, User(userId, userName), tweetText, Place(placeId, placeType, coordinateArr))
    val collector = mock[Collector[Tweet]]
    (collector.collect _).expects(expTweet)

    When("flat map function is called for given json and collector")
    StringToTweetFlatMap().flatMap(json, collector)

    Then("mocked collector should match expected result without any errors")
  }

}
