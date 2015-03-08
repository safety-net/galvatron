package io.github.safety_net.galvatron

import org.specs2.mutable._
import io.github.safety_net.galvatron.BotTests
import scala.io.Source

class BotTestsSpec extends Specification {

  // TODO gitcrypt
  // file with twitterKeys not to be commited!!
  // structure
  // ConsumerKey \n
  // ConsumerSecret \n
  // AccessToken \n
  // AccessTokenSecret
  val filename = "twitterKeys.keys"
  val keyLines = Source.fromFile(filename).getLines().toList
  var botTests: BotTests = null

  sequential

  "BotTestCtor" should {
    "read in keys and construct" in {
      keyLines.size shouldEqual 4
      botTests = new BotTests(keyLines(0),keyLines(1),keyLines(2),keyLines(3))
      botTests should not beNull
    }
  }

  "friendRatioCheck" should{
    "returns results for valid user" in {
      val user = "JosephKirwin"
      val result = botTests.friendRatioCheck(user)
      (result > 0) must beTrue
    }
  }

}
