package io.db

import model.Tweet
import reactivemongo.api.MongoConnection.ParsedURI
import reactivemongo.api.bson.collection.BSONCollection
import reactivemongo.api.commands.WriteResult
import reactivemongo.api.{AsyncDriver, DB, MongoConnection}

import scala.concurrent.ExecutionContext.Implicits.global
import model.implicits.TwitterModelImplicitsContainer._
import reactivemongo.api.bson.BSONDocument

import scala.concurrent.Future

class MongoDriver(mongoURL: String, dbName: String, collectionName: String) {

  private val mongoDriver: AsyncDriver = AsyncDriver()
  private val parsedURIFuture: Future[ParsedURI] = MongoConnection.fromString(mongoURL)
  private val connection: Future[MongoConnection] = parsedURIFuture.flatMap(uri => mongoDriver.connect(uri))
  private val db: Future[DB] = connection.flatMap(_.database(dbName))
  private val tweetsCollection: Future[BSONCollection] = db.map(_.collection(collectionName))

  def createTweet(tweet: Tweet): Future[WriteResult] = {
    tweetsCollection.flatMap(_.insert.one(tweet))
  }

  def loadAllTweets(): Future[Seq[Tweet]] = {
    tweetsCollection.flatMap(_.find(BSONDocument.empty).cursor[Tweet]().collect[Seq]())
  }

  def closeConnection: Future[Unit] = mongoDriver.close()

}
