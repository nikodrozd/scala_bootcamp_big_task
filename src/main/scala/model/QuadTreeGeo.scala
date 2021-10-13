package model

case class QuadTreeGeo(level: Int = 0,
                       center: Coordinate = Coordinate(0.0, 0.0),
                       halfWidth: Double = 180,
                       halfHeight: Double = 90,
                       tweetsInTile: Seq[Tweet] = Seq.empty[Tweet]) extends java.io.Serializable{

  lazy val topLeftNode: QuadTreeGeo = generateChildNodeByPosition("topLeft")
  lazy val topRightNode: QuadTreeGeo = generateChildNodeByPosition("topRight")
  lazy val botLeftNode: QuadTreeGeo = generateChildNodeByPosition("botLeft")
  lazy val botRightNode: QuadTreeGeo = generateChildNodeByPosition("botRight")

  def addTweet(tweet: Tweet): QuadTreeGeo = QuadTreeGeo(level, center, halfWidth, halfHeight, tweetsInTile :+ tweet)

  def getNumberOfPointInTile: Int = tweetsInTile.size

  def getNodesFromLevel(level: Int): Seq[QuadTreeGeo] = level match {
    case this.level               => Seq(this)
    case l if l == this.level + 1 => Seq(topLeftNode, topRightNode, botLeftNode, botRightNode)
    case l                        => topLeftNode.getNodesFromLevel(l) ++
      topRightNode.getNodesFromLevel(l) ++
      botLeftNode.getNodesFromLevel(l) ++
      botRightNode.getNodesFromLevel(l)
  }

  def getMaxNumberOfPointsOnLevel(level: Int): Int = {
    if (level == this.level) this.getNumberOfPointInTile
    else {
      Math.max(
        Math.max(topLeftNode.getMaxNumberOfPointsOnLevel(level),
          topRightNode .getMaxNumberOfPointsOnLevel(level)),
        Math.max(botLeftNode.getMaxNumberOfPointsOnLevel(level),
          botRightNode.getMaxNumberOfPointsOnLevel(level))
      )
    }
  }

  private[model] def generateChildNodeByPosition(position: String): QuadTreeGeo = {
    val newHalfWidth = halfWidth / 2
    val newHalfHeight = halfHeight / 2
    val newCenterCoordinate: Coordinate = position match {
      case "topLeft" => Coordinate(center.x - newHalfWidth, center.y + newHalfHeight)
      case "topRight" => Coordinate(center.x + newHalfWidth, center.y + newHalfHeight)
      case "botLeft" => Coordinate(center.x - newHalfWidth, center.y - newHalfHeight)
      case "botRight" => Coordinate(center.x + newHalfWidth, center.y - newHalfHeight)
    }

    def isInsideTile(tweet: Tweet): Boolean = {
      val coordinate = tweet.place.placeCoordinates.head
      if (coordinate.x >= newCenterCoordinate.x - newHalfWidth
        && coordinate.x < newCenterCoordinate.x + newHalfWidth
        && coordinate.y > newCenterCoordinate.y - newHalfHeight
        && coordinate.y <= newCenterCoordinate.y + newHalfHeight) true
      else false
    }

    QuadTreeGeo(level + 1, newCenterCoordinate, newHalfWidth, newHalfHeight, tweetsInTile.filter(isInsideTile))
  }

}