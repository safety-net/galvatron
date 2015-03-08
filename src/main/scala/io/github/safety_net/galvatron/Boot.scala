package io.github.safety_net.galvatron

import akka.actor.{ActorSystem, Props}
import akka.io.IO
import io.github.safety_net.galvatron.twitterSpy.TwitterSpyClient
import spray.can.Http
import akka.pattern.ask
import akka.util.Timeout
import scala.concurrent.duration._

object Boot extends App {
  // Test client w/ Toby's ID
  val twitterId = 93370723L
  val client= TwitterSpyClient(twitterId)


  // we need an ActorSystem to host our application in
  implicit val system = ActorSystem("on-spray-can")

  // create and start our service actor
  val service = system.actorOf(Props[ThreatAssessmentServiceActor])

  implicit val timeout = Timeout(5.seconds)
  // start a new HTTP server on port 8080 with our service actor as the handler
  IO(Http) ? Http.Bind(service, interface = "localhost", port = 8080)
}
