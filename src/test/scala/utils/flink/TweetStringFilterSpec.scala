package utils.flink

import org.scalatest.GivenWhenThen
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class TweetStringFilterSpec extends AnyFlatSpec with Matchers with GivenWhenThen{

  "TweetStringFilter.filter" should "return true if input json string matches filter criteria" in {
    Given("correct json string")
    val json = """{"created_at":"some date", "place":{"place_type":"poi"}}"""

    When("filter function is called for given json string")
    val res = TweetStringFilter().filter(json)

    Then("result should be true")
    res should equal(true)
  }

  it should "return false if input json string doesn't match filter criteria" in {
    Given("wrong json string")
    val json = """{"deleted":"some date"}"""

    When("filter function is called for given json string")
    val res = TweetStringFilter().filter(json)

    Then("result should be false")
    res should equal(false)
  }

}
