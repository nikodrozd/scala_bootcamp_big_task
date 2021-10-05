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
    val expTopLeftNode: Option[QuadTreeGeo] = None
    val expTopRightNode: Option[QuadTreeGeo] = None
    val expBotLeftNode: Option[QuadTreeGeo] = None
    val expBotRightNode: Option[QuadTreeGeo] = None
    val expPointsInTile: Int = 0

    When("new QuadTreeGeo is created with no parameters")
    val resQuadTreeGeo = QuadTreeGeo()

    Then("values of this QuadTreeGeo should be the same as expected ones")
    resQuadTreeGeo.level should equal(expLevel)
    resQuadTreeGeo.center.x should equal(expCenter.x)
    resQuadTreeGeo.center.y should equal(expCenter.y)
    resQuadTreeGeo.halfWidth should equal(expHalfWidth)
    resQuadTreeGeo.halfHeight should equal(expHalfHeight)
    resQuadTreeGeo.topLeftNode should equal(expTopLeftNode)
    resQuadTreeGeo.topRightNode should equal(expTopRightNode)
    resQuadTreeGeo.botLeftNode should equal(expBotLeftNode)
    resQuadTreeGeo.botRightNode should equal(expBotRightNode)
    resQuadTreeGeo.pointsInTile should equal(expPointsInTile)
  }

  "QuadTreeGeo.getModifiedCopy called w/o input parameters" should "create copy of QuadTreeGeo with same parameters" in {
    Given("QuadTreeGeo object with default values")
    val initQuadTreeGeo = QuadTreeGeo()

    When("new QuadTreeGeo object is created using getModifiedCopy function w/o input parameters")
    val resQuadTreeGeo = initQuadTreeGeo.getModifiedCopy()

    Then("new object should have same parameters except changed ones")
    resQuadTreeGeo.level should equal(initQuadTreeGeo.level)
    resQuadTreeGeo.center.x should equal(initQuadTreeGeo.center.x)
    resQuadTreeGeo.center.y should equal(initQuadTreeGeo.center.y)
    resQuadTreeGeo.halfWidth should equal(initQuadTreeGeo.halfWidth)
    resQuadTreeGeo.halfHeight should equal(initQuadTreeGeo.halfHeight)
    resQuadTreeGeo.topLeftNode should equal(initQuadTreeGeo.topLeftNode)
    resQuadTreeGeo.topRightNode should equal(initQuadTreeGeo.topRightNode)
    resQuadTreeGeo.botLeftNode should equal(initQuadTreeGeo.botLeftNode)
    resQuadTreeGeo.botRightNode should equal(initQuadTreeGeo.botRightNode)
    resQuadTreeGeo.pointsInTile should equal(resQuadTreeGeo.pointsInTile)
  }

  "QuadTreeGeo.getModifiedCopy called with input parameters" should "create copy of QuadTreeGeo with new specified parameters" in {
    Given("QuadTreeGeo object with default values and values for new object")
    val initQuadTreeGeo = QuadTreeGeo()
    val newLevel = 2
    val newCenter: Coordinate = Coordinate(1.0, 1.0)
    val newHalfWidth: Double = 120
    val newHalfHeight: Double = 60
    val newTopLeftNode: Option[QuadTreeGeo] = Some(QuadTreeGeo(level = 3, pointsInTile = 1))
    val newTopRightNode: Option[QuadTreeGeo] = Some(QuadTreeGeo(level = 3, pointsInTile = 2))
    val newBotLeftNode: Option[QuadTreeGeo] = Some(QuadTreeGeo(level = 3, pointsInTile = 3))
    val newBotRightNode: Option[QuadTreeGeo] = Some(QuadTreeGeo(level = 3, pointsInTile = 4))
    val newPointsInTile: Int = 10

    When("new QuadTreeGeo object is created using getModifiedCopy function w/o input parameters")
    val resQuadTreeGeo = initQuadTreeGeo.getModifiedCopy(newLevel, newCenter, newHalfWidth, newHalfHeight,
      newTopLeftNode, newTopRightNode, newBotLeftNode, newBotRightNode, newPointsInTile)

    Then("new object should have same parameters except changed ones")
    resQuadTreeGeo.level should equal(newLevel)
    resQuadTreeGeo.center.x should equal(newCenter.x)
    resQuadTreeGeo.center.y should equal(newCenter.y)
    resQuadTreeGeo.halfWidth should equal(newHalfWidth)
    resQuadTreeGeo.halfHeight should equal(newHalfHeight)
    resQuadTreeGeo.topLeftNode should equal(newTopLeftNode)
    resQuadTreeGeo.topRightNode should equal(newTopRightNode)
    resQuadTreeGeo.botLeftNode should equal(newBotLeftNode)
    resQuadTreeGeo.botRightNode should equal(newBotRightNode)
    resQuadTreeGeo.pointsInTile should equal(newPointsInTile)
  }

  "QuadTreeGeo.generateChildrenTillLevel" should "create child QuadTreeGeo nodes of initial QuadTreeGeo object till specified level with proper parameters" in {
    Given("initial QuadTreeGeo object and level value")
    val initQuadTreeGeo: QuadTreeGeo = QuadTreeGeo()
    val level = 1
    val newHalfWidth = initQuadTreeGeo.halfWidth / 2
    val newHalfHeight = initQuadTreeGeo.halfHeight / 2
    val expTopLeftNode = Some(QuadTreeGeo(level, Coordinate(initQuadTreeGeo.center.x - newHalfWidth, initQuadTreeGeo.center.y + newHalfHeight), newHalfWidth, newHalfHeight))
    val expTopRightNode = Some(QuadTreeGeo(level, Coordinate(initQuadTreeGeo.center.x + newHalfWidth, initQuadTreeGeo.center.y + newHalfHeight), newHalfWidth, newHalfHeight))
    val expBotLeftNode = Some(QuadTreeGeo(level, Coordinate(initQuadTreeGeo.center.x - newHalfWidth, initQuadTreeGeo.center.y - newHalfHeight), newHalfWidth, newHalfHeight))
    val expBotRightNode = Some(QuadTreeGeo(level, Coordinate(initQuadTreeGeo.center.x + newHalfWidth, initQuadTreeGeo.center.y - newHalfHeight), newHalfWidth, newHalfHeight))

    When("generateChildrenTillLevel function is called for QuadTreeGeo object with specified level")
    val resQuadTreeGeo = initQuadTreeGeo.generateChildrenTillLevel(level)

    Then("new QuadTreeGeo object should have generated children with proper parameters till specified level")
    resQuadTreeGeo.topLeftNode should equal(expTopLeftNode)
    resQuadTreeGeo.topRightNode should equal(expTopRightNode)
    resQuadTreeGeo.botLeftNode should equal(expBotLeftNode)
    resQuadTreeGeo.botRightNode should equal(expBotRightNode)
  }

  "QuadTreeGeo.analyzeAndCountCoordinate" should "compare provided Coordinate with QuadTreeGeo nodes coordinates and increase count of points in tile for matched one" in {
    Given("QuadTreeGeo with children till level 1 and input Coordinate")
    val quadTreeGeo = QuadTreeGeo().generateChildrenTillLevel(1)
    val coordinate = Coordinate(-100.0, 80)

    When("analyzeAndCountCoordinate function is called for input QuadTreeGeo with given Coordinate")
    val resQuadTreeGeo = quadTreeGeo.analyzeAndCountCoordinate(coordinate)

    Then("points in tile parameter of proper node should be increase by 1")
    resQuadTreeGeo.topLeftNode.map(_.pointsInTile).getOrElse(-1) should equal(1)
    resQuadTreeGeo.topRightNode.map(_.pointsInTile).getOrElse(-1) should equal(0)
    resQuadTreeGeo.botLeftNode.map(_.pointsInTile).getOrElse(-1) should equal(0)
    resQuadTreeGeo.botRightNode.map(_.pointsInTile).getOrElse(-1) should equal(0)
  }

  "QuadTreeGeo.getNodesFromLevel" should "return sequence of QuadTreeNodes from specified level" in {
    Given("QuadTreeGeo with children till some level and expected nodes list")
    val level = 1
    val quadTreeGeo = QuadTreeGeo().generateChildrenTillLevel(level)
    val expRes = List(QuadTreeGeo(1,Coordinate(-90.0,45.0),90.0,45.0),
      QuadTreeGeo(1,Coordinate(90.0,45.0),90.0,45.0),
      QuadTreeGeo(1,Coordinate(-90.0,-45.0),90.0,45.0),
      QuadTreeGeo(1,Coordinate(90.0,-45.0),90.0,45.0))

    When("getNodesFromLevel is called for given QuadTreeGeo")
    val res = quadTreeGeo.getNodesFromLevel(level)

    Then("result should be list of child node from level 1")
    res should equal(expRes)
  }

  "QuadTreeGeo.getMaxNumberOfPointsOnLevel" should "return max number of nodes' points in tile parameter from specified level" in {
    Given("QuadTreeGeo with children till some level, some Coordinates and expected max value")
    val level = 1
    val expMaxPointsInTile = 3
    val coordinateSeq = Seq(Coordinate(-100.0, 80), Coordinate(100.0, 80), Coordinate(-100.0, -80),
      Coordinate(-120.0, 60), Coordinate(-5.0, 80), Coordinate(120.0, 60))
    val quadTreeGeoWithPoints = coordinateSeq.foldLeft(QuadTreeGeo().generateChildrenTillLevel(level)){
      (qt, coordinate) => qt.analyzeAndCountCoordinate(coordinate)
    }

    When("getMaxNumberOfPointsOnLevel function is called for given QuadTreeGeo")
    val result = quadTreeGeoWithPoints.getMaxNumberOfPointsOnLevel(level)

    Then("result should be same as given expected result")
    result should equal(expMaxPointsInTile)
  }

}
