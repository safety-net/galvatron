package io.github.safety_net.galvatron

import com.ning.http.client._
import twitter4j.{User, ResponseList, TwitterFactory, Twitter}
import twitter4j.conf.ConfigurationBuilder

class BotTests(consumerKey:String,consumerSecret:String,accessToken:String,accessTokenSecret:String) {

  // (1) config work to create a twitter object
  val cb = new ConfigurationBuilder()
  cb.setDebugEnabled(true)
    .setOAuthConsumerKey(consumerKey)
    .setOAuthConsumerSecret(consumerSecret)
    .setOAuthAccessToken(accessToken)
    .setOAuthAccessTokenSecret(accessTokenSecret)
  val tf = new TwitterFactory(cb.build())
  val twitter = tf.getInstance()

  def friendRatioCheck(username: String): Double = {

    val user = twitter.showUser(username)
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
