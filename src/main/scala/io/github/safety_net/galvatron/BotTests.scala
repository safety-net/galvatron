package io.github.safety_net.galvatron

import twitter4j._
import twitter4j.conf.ConfigurationBuilder

// https://dev.twitter.com/rest/public/rate-limiting
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

  def reciprocationPercentage(username: String): Double = {

    val user: User = twitter.showUser(username)
    val followerCount = user.getFollowersCount()
    val friendCount = user.getFriendsCount()

    // We don't want to deal with the no friend or followers case
    if (friendCount == 0 || followerCount == 0)
      return 0

    // If this percentage is low then it means the
    // user is following much more than are following them.
    (followerCount.toDouble) / friendCount
  }

  def maxDupeTweetCount(username: String): Int = {
    val tweets: ResponseList[Status] = twitter.getUserTimeline(username)
    println("Tweet count returned: %d".format(tweets.size()))

    val list: List[Status] = for(
      i <- (0 until tweets.size()).toList;
      status = tweets.get(i)
    ) yield status

    //We don't want retweets or any posts with urls for this type of detection
    val trimmed: List[String] = list.filter(status => status.getURLEntities().isEmpty && !status.isRetweet()).map(x => x.getText())
    println("Trimmed tweet count returned: %d".format(trimmed.length))
    println("Sample tweet %s".format(trimmed(0)))
    if(trimmed.isEmpty)
      return 0
    val query: Query = new Query(trimmed(0))
    val searchResult: QueryResult = twitter.search(query)
    searchResult.getCount()
  }

  def isBot(username: String) : Boolean = {
    //TODO - improve decision logic via some stats or machine learning.
    //Current Tests:
    val dupes = maxDupeTweetCount(username)
    if(dupes > 10)
       return true
    val recipPer = reciprocationPercentage(username)
      if(dupes > 1 && recipPer < 0.1)
        return true
    false
  }
}
