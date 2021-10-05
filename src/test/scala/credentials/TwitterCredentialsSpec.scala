package credentials

import org.apache.flink.streaming.connectors.twitter.TwitterSource
import org.scalatest.GivenWhenThen
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class TwitterCredentialsSpec extends AnyFlatSpec with Matchers with GivenWhenThen{

  "TwitterCredentials.getTwitterCredentials function" should "return Properties object with non empty CONSUMER_KEY, CONSUMER_SECRET, TOKEN and TOKEN_SECRET" in {
    Given("Properties object taken from TwitterCredentials object and default test values of CONSUMER_KEY, CONSUMER_SECRET, TOKEN and TOKEN_SECRET")
    val props = TwitterCredentials.getTwitterCredentials
    val defaultConsumerKey = "consumerKey is missed"
    val defaultConsumerSecret = "consumerSecret is missed"
    val defaultToken = "token is missed"
    val defaultTokenSecret = "tokenSecret is missed"

    When("CONSUMER_KEY, CONSUMER_SECRET, TOKEN and TOKEN_SECRET are taken from given properties with possible default values")
    val consumerKey = props.getProperty(TwitterSource.CONSUMER_KEY, defaultConsumerKey)
    val consumerSecret = props.getProperty(TwitterSource.CONSUMER_SECRET, defaultConsumerSecret)
    val token = props.getProperty(TwitterSource.TOKEN, defaultToken)
    val tokenSecret = props.getProperty(TwitterSource.TOKEN_SECRET, defaultTokenSecret)

    Then("result values should not be equal to default ones")
    consumerKey should not equal(defaultConsumerKey)
    consumerSecret should not equal(defaultConsumerSecret)
    token should not equal(defaultToken)
    tokenSecret should not equal(defaultTokenSecret)
  }

}
