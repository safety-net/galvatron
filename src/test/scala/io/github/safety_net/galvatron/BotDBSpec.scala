package io.github.safety_net.galvatron

import org.specs2.mutable.Specification
import io.github.safety_net.galvatron.BotDB

class BotDBSpec extends Specification {

  "ctor test" should {
    "create" in {
      val bdb = new BotDB
      0 shouldEqual(0)
    }
  }

}
