package credentials

import org.scalatest.GivenWhenThen
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class MongoDBCredentialsSpec extends AnyFlatSpec with Matchers with GivenWhenThen{

  "MongoDBCredentials" should "have same host and db name values in connection URLs with auth and without" in {
    Given("connection URLs with and without auth taken from MongoDBCredentials class")
    val url = MongoDBCredentials.mongoURL
    val urlWithAuth = MongoDBCredentials.mongoURLWithAuthData

    When("host and db name values are taken from these URLs")
    val hostAndDbFromUrl = url.substring(url.indexOf("//") + 2, url.length)
    val hostAndDbFromUrlWithAuth = urlWithAuth.substring(urlWithAuth.indexOf("@") + 1, urlWithAuth.indexOf("?"))

    Then("taken values should be the same for both URLs")
    hostAndDbFromUrl should equal(hostAndDbFromUrlWithAuth)
  }

  it should "have same values of host, db name, db user, db pass in appropriate variables and connection URL with auth" in {
    Given("connection URL with auth and host, db name, db user, db pass values")
    val host = MongoDBCredentials.host
    val port = MongoDBCredentials.port
    val dbName = MongoDBCredentials.dbName
    val dbUser = MongoDBCredentials.dbUser
    val dbPass = MongoDBCredentials.dbPass
    val urlWithAuth = MongoDBCredentials.mongoURLWithAuthData

    When("another connection url with auth data is created based on given host, db name, db user, db pass values")
    val anotherUrlWithAuth = s"mongodb://$dbUser:$dbPass@$host:$port/$dbName?authSource=$dbName"

    Then("it should be the same as connection url with auth from MongoDBCredentials")
    anotherUrlWithAuth should equal(urlWithAuth)
  }

}
