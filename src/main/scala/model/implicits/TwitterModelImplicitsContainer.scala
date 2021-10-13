package model.implicits

import model.{Coordinate, Place, Tweet, User}
import reactivemongo.api.bson.{BSONDocumentReader, BSONDocumentWriter, BSONReader, BSONWriter, Macros}

object TwitterModelImplicitsContainer {

  /**
   * BSONWriters for MongoDriver
   */
  implicit val coordinateWriter: BSONWriter[Coordinate] = Macros.writer[Coordinate]
  implicit val placeWriter: BSONWriter[Place] = Macros.writer[Place]
  implicit val userWriter: BSONWriter[User] = Macros.writer[User]
  implicit val tweetWriter: BSONDocumentWriter[Tweet] = Macros.writer[Tweet]

  /**
   * BSONReaders for MongoDriver
   */
  implicit val coordinateReader: BSONReader[Coordinate] = Macros.reader[Coordinate]
  implicit val placeReader: BSONReader[Place] = Macros.reader[Place]
  implicit val userReader: BSONReader[User] = Macros.reader[User]
  implicit val tweetReader: BSONDocumentReader[Tweet] = Macros.reader[Tweet]

}
