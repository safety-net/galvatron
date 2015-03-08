package io.github.safety_net.galvatron

import twitter4j._
import twitter4j.conf.ConfigurationBuilder

class BotTests(consumerKey:String,consumerSecret:String,accessToken:String,accessTokenSecret:String) {

  // create a twitter object
  val cb = new ConfigurationBuilder()
  cb.setDebugEnabled(true)
    .setOAuthConsumerKey(consumerKey)
    .setOAuthConsumerSecret(consumerSecret)
    .setOAuthAccessToken(accessToken)
    .setOAuthAccessTokenSecret(accessTokenSecret)
  val tf = new TwitterFactory(cb.build())
  val twitter = tf.getInstance()
  println(twitter)

  def friendRatioCheck(username: String): Double = {

    val user: User = twitter.showUser(username)
    val followerCount = user.getFollowersCount()
    val friendCount = user.getFriendsCount()

    // We don't want to deal with the no friend or followers case
    if (followerCount == 0 || friendCount == 0)
      return 0

    val ratio:Double = friendCount / followerCount
    return ratio
  }

  def postDupes(username: String): Int = {
    return 0
  }
}
