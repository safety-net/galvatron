package io.github.safety_net.galvatron.twitterSpy.sdk

import twitter4j._

import scala.annotation.tailrec

object TwitterClient {
  val twitterClient = TwitterFactory.getSingleton

  def allFollowers(): List[Long] = {
    val ffRes = twitterClient.friendsFollowers()

    @tailrec
    def go(cursor: Long, acc: List[Long]): List[Long] = {
      val ids = ffRes.getFollowersIDs(cursor)

      val l = acc ++ ids.getIDs

      if (!ids.hasNext)
        l
      else
        go(ids.getNextCursor, l)
    }

    go(-1, List())
  }
}
