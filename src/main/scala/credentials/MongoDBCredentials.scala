package credentials

import com.typesafe.config.ConfigFactory

object MongoDBCredentials {

  val host: String = ConfigFactory.load().getString("mongodb.host")
  val port: String = ConfigFactory.load().getString("mongodb.port")
  val dbUser: String = ConfigFactory.load().getString("mongodb.dbUser")
  val dbPass: String = ConfigFactory.load().getString("mongodb.dbPass")
  val dbName: String = ConfigFactory.load().getString("mongodb.dbName")
  val collectionName: String = ConfigFactory.load().getString("mongodb.collectionName")

  val mongoURLWithAuthData: String = s"mongodb://$dbUser:$dbPass@$host:$port/$dbName?authSource=$dbName"
  val mongoURLWithAuthDataAndCollection = s"mongodb://$dbUser:$dbPass@$host:$port/$dbName.$collectionName?authSource=$dbName"
  val mongoURL: String = s"mongodb://$host:$port/$dbName"

}
