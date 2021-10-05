package model

case class QuadTreeGeo(level: Int = 0,
                       center: Coordinate = Coordinate(0.0, 0.0),
                       halfWidth: Double = 180,
                       halfHeight: Double = 90,
                       topLeftNode: Option[QuadTreeGeo] = None,
                       topRightNode: Option[QuadTreeGeo] = None,
                       botLeftNode: Option[QuadTreeGeo] = None,
                       botRightNode: Option[QuadTreeGeo] = None,
                       pointsInTile: Int = 0) extends java.io.Serializable{

  def generateChildrenTillLevel(level: Int): QuadTreeGeo = {
    if (this.level < level) {
      val nextLevel = this.level + 1
      val newHalfWidth = this.halfWidth / 2
      val newHalfHeight = this.halfHeight / 2

      val topLeftNodeNew = QuadTreeGeo(nextLevel, Coordinate(center.x - newHalfWidth, center.y + newHalfHeight), newHalfWidth, newHalfHeight)
        .generateChildrenTillLevel(level)
      val topRightNodeNew = QuadTreeGeo(nextLevel, Coordinate(center.x + newHalfWidth, center.y + newHalfHeight), newHalfWidth, newHalfHeight)
        .generateChildrenTillLevel(level)
      val botLeftNodeNew = QuadTreeGeo(nextLevel, Coordinate(center.x - newHalfWidth, center.y - newHalfHeight), newHalfWidth, newHalfHeight)
        .generateChildrenTillLevel(level)
      val botRightNodeNew = QuadTreeGeo(nextLevel, Coordinate(center.x + newHalfWidth, center.y - newHalfHeight), newHalfWidth, newHalfHeight)
        .generateChildrenTillLevel(level)

      getModifiedCopy(topLeftNode = Some(topLeftNodeNew),
        topRightNode = Some(topRightNodeNew),
        botLeftNode = Some(botLeftNodeNew),
        botRightNode = Some(botRightNodeNew))
    } else this
  }

  private[model] def getModifiedCopy(level: Int = this.level,
                              center: Coordinate = this.center,
                              halfWidth: Double = this.halfWidth,
                              halfHeight: Double = this.halfHeight,
                              topLeftNode: Option[QuadTreeGeo] = this.topLeftNode,
                              topRightNode: Option[QuadTreeGeo] = this.topRightNode,
                              botLeftNode: Option[QuadTreeGeo] = this.botLeftNode,
                              botRightNode: Option[QuadTreeGeo] = this.botRightNode,
                              pointsInTile: Int = this.pointsInTile): QuadTreeGeo = {
    QuadTreeGeo(level, center, halfWidth, halfHeight, topLeftNode, topRightNode, botLeftNode, botRightNode, pointsInTile)
  }

  def analyzeAndCountCoordinate(c: Coordinate): QuadTreeGeo = c match {
    case Coordinate(x, y) if x <= center.x && y > center.y =>
      val topLeftNodeNew = topLeftNode.map(_.analyzeAndCountCoordinate(c))
      getModifiedCopy(topLeftNode = topLeftNodeNew, pointsInTile = this.pointsInTile + 1)
    case Coordinate(x, y) if x > center.x && y >= center.y =>
      val topRightNodeNew = topRightNode.map(_.analyzeAndCountCoordinate(c))
      getModifiedCopy(topRightNode = topRightNodeNew, pointsInTile = this.pointsInTile + 1)
    case Coordinate(x, y) if x < center.x && y <= center.y =>
      val botLeftNodeNew = botLeftNode.map(_.analyzeAndCountCoordinate(c))
      getModifiedCopy(botLeftNode = botLeftNodeNew, pointsInTile = this.pointsInTile + 1)
    case Coordinate(x, y) if x >= center.x && y < center.y =>
      val botRightNodeNew = botRightNode.map(_.analyzeAndCountCoordinate(c))
      getModifiedCopy(botRightNode = botRightNodeNew, pointsInTile = this.pointsInTile + 1)
  }

  def getNodesFromLevel(level: Int): Seq[QuadTreeGeo] = level match {
      case this.level               => Seq(this)
      case l if l == this.level + 1 => Seq(topLeftNode, topRightNode, botLeftNode, botRightNode).flatten
      case l                        => topLeftNode.map(_.getNodesFromLevel(l)).getOrElse(Seq.empty) ++
        topRightNode.map(_.getNodesFromLevel(l)).getOrElse(Seq.empty) ++
        botLeftNode.map(_.getNodesFromLevel(l)).getOrElse(Seq.empty) ++
        botRightNode.map(_.getNodesFromLevel(l)).getOrElse(Seq.empty)
    }

  def getMaxNumberOfPointsOnLevel(level: Int): Int = {
    if (level == this.level) this.pointsInTile
    else {
      Math.max(
        Math.max(topLeftNode.map(_.getMaxNumberOfPointsOnLevel(level)).getOrElse(0),
          topRightNode.map(_.getMaxNumberOfPointsOnLevel(level)).getOrElse(0)),
        Math.max(botLeftNode.map(_.getMaxNumberOfPointsOnLevel(level)).getOrElse(0),
          botRightNode.map(_.getMaxNumberOfPointsOnLevel(level)).getOrElse(0))
      )
    }
  }

}