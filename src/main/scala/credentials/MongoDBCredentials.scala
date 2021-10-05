package credentials

object MongoDBCredentials {

  val host = "localhost:27017"
  val dbUser = "mongoadmin"
  val dbPass = "secret"
  val dbName = "mongotwitter"
  val collectionName = "tweets"

  val mongoURLWithAuthData: String = s"mongodb://$dbUser:$dbPass@$host/$dbName?authSource=$dbName"
  val mongoURL: String = s"mongodb://$host/$dbName"

}
