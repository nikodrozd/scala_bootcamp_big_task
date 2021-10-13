package credentials

import com.typesafe.config.ConfigFactory
import org.apache.flink.streaming.connectors.twitter.TwitterSource

import java.util.Properties

object TwitterCredentials {

  private val consumerKey = ConfigFactory.load().getString("twitter.consumerKey")
  private val consumerSecret = ConfigFactory.load().getString("twitter.consumerSecret")
  private val token = ConfigFactory.load().getString("twitter.token")
  private val tokenSecret = ConfigFactory.load().getString("twitter.tokenSecret")

  def getTwitterCredentials: Properties = {
    val props = new Properties()
    props.setProperty(TwitterSource.CONSUMER_KEY, consumerKey)
    props.setProperty(TwitterSource.CONSUMER_SECRET, consumerSecret)
    props.setProperty(TwitterSource.TOKEN, token)
    props.setProperty(TwitterSource.TOKEN_SECRET, tokenSecret)
    props
  }

}
