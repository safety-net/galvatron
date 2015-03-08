package io.github.safety_net.galvatron.twitterSpy

import akka.actor._
import io.github.safety_net.galvatron.twitterSpy.TwitterSpyClientActor.{DetectedNewFollower, CheckForNewFollowers}
import io.github.safety_net.galvatron.twitterSpy.sdk.TwitterClient

import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

object TwitterSpyClient {
  val system = ActorSystem("twitter-spy-system")

  var activeClients: Map[Long, TwitterSpyClient] = Map()

  def apply(twitterId: Long) = {
    activeClients.get(twitterId) match {
      case Some(client) => client
      case None =>
        val client = new TwitterSpyClient(system, twitterId)
        activeClients += twitterId -> client
        client
    }
  }
}

class TwitterSpyClient(system: ActorSystem, twitterId: Long) {
  val clientActor = system.actorOf(TwitterSpyClientActor.props(twitterId))
}

object TwitterSpyClientActor {
  def props(twitterId: Long) = Props(new TwitterSpyClientActor(twitterId))

  case object CheckForNewFollowers

  case class DetectedNewFollower(followerId: Long)
}

class TwitterSpyClientActor(twitterId: Long) extends Actor {
  val twitter = TwitterClient

  var previousFollowerIds: Set[Long] = null

  override def preStart() = {
    context.system.scheduler.schedule(0 seconds, 10 minutes, self, CheckForNewFollowers)
  }

  def receive = {
    case CheckForNewFollowers =>
      println(s"Looking up followers for Twitter ID: $twitterId")
      val followers = lookupFollowers(twitterId)

      println(s"Found followers: $followers")

      // If this is the initial check just use what we've fetched
      if (previousFollowerIds == null)
        previousFollowerIds = followers

      val newFollowers = followers.diff(previousFollowerIds)

      println(s"New followers: $newFollowers")

      newFollowers.foreach(followerId => self ! DetectedNewFollower(followerId))

      previousFollowerIds = followers
    case DetectedNewFollower(followerId) =>
      val isBot = checkIsBot(followerId)

      if (isBot)
        blockUser(followerId)
  }

  private def lookupFollowers(twitterId: Long): Set[Long] =
    TwitterClient.allFollowers().toSet

  private def checkIsBot(twitterId: Long): Boolean = ???

  private def blockUser(twitterId: Long): Unit = ???
}
