package model

case class Tweet(id: Long, dateTime: String, user: User, text: String, place: Place)

case class User(user_id: Long, name: String)

case class Coordinate(x: Double, y: Double)

case class Place(place_id: String, placeType: String, placeCoordinates: Seq[Coordinate]) {
  def getPointCoordinateAsSeq: Seq[Double] = Seq(placeCoordinates.head.x,placeCoordinates.head.y)
}