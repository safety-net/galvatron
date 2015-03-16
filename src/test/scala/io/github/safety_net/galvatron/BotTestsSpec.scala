package io.github.safety_net.galvatron

import org.specs2.mutable._
import scala.io.Source

class BotTestsSpec extends Specification {

  lazy val lzBotTests = {
    // TODO gitcrypt
    // file with twitterKeys not to be committed!!
    // structure
    // ConsumerKey \n
    // ConsumerSecret \n
    // AccessToken \n
    // AccessTokenSecret
    val filename = "src/test/resources/twitterKeys.keys"
    val keyLines = Source.fromFile(filename).getLines().toList
    new BotTests(keyLines(0), keyLines(1), keyLines(2), keyLines(3))
  }

  "isBot" should {
    "determine Vanessa_Daviss is a bot" in {
      val username = "Vanessa_Daviss" // Some bot user
      val result = lzBotTests.isBot(username)
      if (result)
        println(s"user: '$username' is a bot")
        println()
      result must beTrue
    }
  }

  "reciprocationPercentage" should {
    "return results for valid user" in {
      val username = "dmoran850" // Some bot user
      val result = lzBotTests.reciprocationPercentage(username)
      println(f"Reciprocation percentage: $result%.3f")
      println()
      (result > 0) must beTrue
    }
  }

  "maxDupeTweetCount" should {
    "return dupe quote count for bot user" in {
      val username = "Vanessa_Daviss" // Some bot user
      val result = lzBotTests.maxDupeTweetCount(username)
      println(s"Dupe count: $result")
      println()
      (result > 0) must beTrue
    }
  }
}

