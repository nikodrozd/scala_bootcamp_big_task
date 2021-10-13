package model

import org.scalatest.GivenWhenThen
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class QuadTreeGeoSpec extends AnyFlatSpec with Matchers with GivenWhenThen{

  "QuadTreeGeo" should "be created with defaults if no parameters were specified" in {
    Given("expected default values for QuadTreeGeo class")
    val expLevel = 0
    val expCenter: Coordinate = Coordinate(0.0, 0.0)
    val expHalfWidth: Double = 180
    val expHalfHeight: Double = 90
    val expTweetsInTile: Seq[Tweet] = Seq.empty[Tweet]

    When("new QuadTreeGeo is created with no parameters")
    val resQuadTreeGeo = QuadTreeGeo()

    Then("values of this QuadTreeGeo should be the same as expected ones")
    resQuadTreeGeo.level should equal(expLevel)
    resQuadTreeGeo.center.x should equal(expCenter.x)
    resQuadTreeGeo.center.y should equal(expCenter.y)
    resQuadTreeGeo.halfWidth should equal(expHalfWidth)
    resQuadTreeGeo.halfHeight should equal(expHalfHeight)
    resQuadTreeGeo.tweetsInTile should equal(expTweetsInTile)
  }

  "QuadTreeGeo.addTweet" should "add given tweet to seq of tweets in tile" in {
    Given("QuadTreeGeo object with default values and tweet object")
    val initQuadTreeGeo = QuadTreeGeo()
    val tweet: Tweet = Tweet(10L, "27/09/2021", User(20L, "user_name"), "some text", Place("testId1", "poi", Seq(Coordinate(-100.0, 20.0))))

    When("given tweet is added to quad tree using addTweet function")
    val resQuadTreeGeo = initQuadTreeGeo.addTweet(tweet)

    Then("seq of tweets in tile of result quad tree should contain given tweet")
    resQuadTreeGeo.tweetsInTile should contain(tweet)
  }

  "QuadTreeGeo.getNumberOfPointInTile" should "return number of tweets in tile of quad tree" in {
    Given("QuadTreeGeo object with default values and tweet object added to this QuadTreeGeo")
    val initQuadTreeGeo = QuadTreeGeo()
    val tweet: Tweet = Tweet(10L, "27/09/2021", User(20L, "user_name"), "some text", Place("testId1", "poi", Seq(Coordinate(-100.0, 20.0))))
    val resQuadTreeGeo = initQuadTreeGeo.addTweet(tweet)
    val expSize = 1

    When("getNumberOfPointInTile function is called for quad tree with tweet")
    val result = resQuadTreeGeo.getNumberOfPointInTile

    Then("seq of tweets in tile of result quad tree should contain given tweet")
    result should equal(expSize)
  }

  "QuadTreeGeo.getNodesFromLevel" should "return sequence of QuadTreeNodes from specified level" in {
    Given("QuadTreeGeo and expected nodes list")
    val level = 1
    val quadTreeGeo = QuadTreeGeo()
    val expRes = List(QuadTreeGeo(1,Coordinate(-90.0,45.0),90.0,45.0),
      QuadTreeGeo(1,Coordinate(90.0,45.0),90.0,45.0),
      QuadTreeGeo(1,Coordinate(-90.0,-45.0),90.0,45.0),
      QuadTreeGeo(1,Coordinate(90.0,-45.0),90.0,45.0))

    When("getNodesFromLevel is called for given QuadTreeGeo")
    val res = quadTreeGeo.getNodesFromLevel(level)

    Then("result should be list of child node from level 1")
    res should equal(expRes)
  }

  "QuadTreeGeo.getMaxNumberOfPointsOnLevel" should "return max number of nodes' points in tile from specified level" in {
    Given("QuadTreeGeo, some Coordinates and expected max value")
    val level = 1
    val expMaxPointsInTile = 3
    val tweetSeq = Seq(
      Tweet(1L, "", User(11L, ""), "", Place("", "", Seq(Coordinate(-100.0, 80)))),
      Tweet(1L, "", User(11L, ""), "", Place("", "", Seq(Coordinate(100.0, 80)))),
      Tweet(1L, "", User(11L, ""), "", Place("", "", Seq(Coordinate(-100.0, -80)))),
      Tweet(1L, "", User(11L, ""), "", Place("", "", Seq(Coordinate(-120.0, 60)))),
      Tweet(1L, "", User(11L, ""), "", Place("", "", Seq(Coordinate(-5.0, 80)))),
      Tweet(1L, "", User(11L, ""), "", Place("", "", Seq(Coordinate(120.0, 60)))))
    val quadTreeGeoWithPoints = tweetSeq.foldLeft(QuadTreeGeo()){
      (qt, tweet) => qt.addTweet(tweet)
    }

    When("getMaxNumberOfPointsOnLevel function is called for given QuadTreeGeo")
    val result = quadTreeGeoWithPoints.getMaxNumberOfPointsOnLevel(level)

    Then("result should be same as given expected result")
    result should equal(expMaxPointsInTile)
  }

}
