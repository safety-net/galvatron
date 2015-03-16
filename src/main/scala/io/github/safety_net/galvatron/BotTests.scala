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
    //println("Tweet count returned: %d".format(tweets.size()))

    val list: List[Status] = for(
      i <- (0 until tweets.size()).toList;
      status = tweets.get(i)
    ) yield status

    if(list.isEmpty)
      return 0

    //We don't want retweets or any posts with urls for this type of detection
    val trimmed: List[String] = list.filter(status => status.getURLEntities().isEmpty && !status.isRetweet()).map(x => x.getText())
    if(trimmed.isEmpty)
      return 0
    println("Sample tweet for <%s>: %s".format(username,trimmed(0)))

    // TODO - increase to query top 3
    val query: Query = new Query(trimmed(0))
    // max count you can return in one fetch is 100, default is 15
    // http://stackoverflow.com/questions/20850712/twitter-query-result-returns-only-15-tweets
    query.count(100)
    val searchResult: QueryResult = twitter.search(query)
    searchResult.getTweets().size()
  }

  // Uses Structured Machine Learning unless optionally disabled.
  def isBot(username: String, noML: Boolean = false) : Boolean = {
    //Current Tests:
    val dupeTweets = maxDupeTweetCount(username)
    val recipPercent = reciprocationPercentage(username)
    if(noML)
      return isBotNoML(username,dupeTweets,recipPercent)

    DataProcessing.normalEquation(dupeTweets,recipPercent)
  }

  private def isBotNoML(username: String, dupeTweets: Double, recipPercent: Double) : Boolean = {
    if(dupeTweets > 10)
      return true
    if(dupeTweets > 1 && recipPercent < 0.1)
      return true
    false
  }
}
