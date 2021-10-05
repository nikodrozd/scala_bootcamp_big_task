package credentials

import org.apache.flink.streaming.connectors.twitter.TwitterSource

import java.util.Properties

object TwitterCredentials {

  private val consumerKey = "bBDkUPIwURjnkY39mK2sfUV72"
  private val consumerSecret = "OTZB2HthWGjxs4dttEIcCn4OBLwucg4lQ8iGd02moOB35poTp5"
  private val token = "1399843040450199553-phHLRMPvPMG6uittibZe9jcZed7Jq7"
  private val tokenSecret = "Mhd773uA2h5GLBoberL9JFbeKt6SEFh0lrznNlqTEWf9c"

  def getTwitterCredentials: Properties = {
    val props = new Properties()
    props.setProperty(TwitterSource.CONSUMER_KEY, consumerKey)
    props.setProperty(TwitterSource.CONSUMER_SECRET, consumerSecret)
    props.setProperty(TwitterSource.TOKEN, token)
    props.setProperty(TwitterSource.TOKEN_SECRET, tokenSecret)
    props
  }

}
