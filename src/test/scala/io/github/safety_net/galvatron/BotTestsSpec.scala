package io.github.safety_net.galvatron

import java.io.File

import org.specs2.mutable._
import scala.io.Source

class BotTestsSpec extends Specification {


  "friendRatioCheck" should{
    "returns results for valid user" in {
      // TODO gitcrypt
      // file with twitterKeys not to be commited!!
      // structure
      // ConsumerKey \n
      // ConsumerSecret \n
      // AccessToken \n
      // AccessTokenSecret
      val filename = "src/test/resources/twitterKeys.keys"
      val keyLines = Source.fromFile(filename).getLines().toList
      val botTests = new BotTests(keyLines(0),keyLines(1),keyLines(2),keyLines(3))
      // SomeBot user
      val user = "dmoran850"
      val result = botTests.friendRatioCheck(user)
      println(result)
      println()
      (result > 0) must beTrue
    }
  }
}
