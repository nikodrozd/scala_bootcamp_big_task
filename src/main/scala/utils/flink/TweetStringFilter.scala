package utils.flink

import org.apache.flink.api.common.functions.FilterFunction
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.{JsonNode, ObjectMapper}

case class TweetStringFilter() extends FilterFunction[String] {
  override def filter(value: String): Boolean = {
    val jsonParser = new ObjectMapper()
    val tweetNode = jsonParser.readValue(value, classOf[JsonNode])
    if (tweetNode.has("created_at") && tweetNode.hasNonNull("place"))
      if (tweetNode.get("place").get("place_type").asText() == "poi") true
      else false
    else false
  }
}
