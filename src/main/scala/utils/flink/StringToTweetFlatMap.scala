package utils.flink

import model.{Coordinate, Place, Tweet, User}
import org.apache.flink.api.common.functions.FlatMapFunction
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.node.ArrayNode
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.{JsonNode, ObjectMapper}
import org.apache.flink.util.Collector

import scala.collection.JavaConverters._

case class StringToTweetFlatMap() extends FlatMapFunction[String, Tweet] {

  override def flatMap(value: String, out: Collector[Tweet]): Unit = {
    val jsonParser: ObjectMapper = new ObjectMapper()
    val tweet: Tweet = parseTweet(jsonParser.readValue(value, classOf[JsonNode]))
    out.collect(tweet)
  }

  private[flink] def parseTweet(tweetNode: JsonNode): Tweet = {
    val id: Long = tweetNode.get("id").asLong()
    val dateTime: String = tweetNode.get("created_at").asText()
    val text: String = tweetNode.get("text").asText()
    val user: User = parseUser(tweetNode.get("user"))
    val place: Place = parsePlace(tweetNode.get("place"))
    Tweet(id, dateTime, user, text, place)
  }

  private[flink] def parseUser(userNode: JsonNode): User = {
    val user_id: Long = userNode.get("id").asLong()
    val user_name: String = userNode.get("name").asText()
    User(user_id, user_name)
  }

  private[flink] def parsePlace(placeNode: JsonNode): Place = {
    val place_id = placeNode.get("id").asText()
    val placeType = placeNode.get("place_type").asText()
    val coordinates: Array[Coordinate] = parseCoordinates(Option(placeNode.get("bounding_box").findValue("coordinates")))
    Place(place_id, placeType, coordinates)
  }

  private[flink] def parseCoordinates(coordinateNodeOpt: Option[JsonNode]): Array[Coordinate] = {
     coordinateNodeOpt match {
      case Some(values: ArrayNode) => values.elements().next().elements().asScala.map(jsonNodeToCoordinate).toArray
      case _ => Array.empty
    }
  }

  private[flink] def jsonNodeToCoordinate(node: JsonNode): Coordinate = Coordinate(node.get(0).asDouble(), node.get(1).asDouble())

}
