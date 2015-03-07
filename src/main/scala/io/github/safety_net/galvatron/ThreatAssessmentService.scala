package io.github.safety_net.galvatron

import akka.actor.Actor
import io.github.safety_net.galvatron.models.ThreatAssessment
import spray.routing._
import spray.http._
import MediaTypes._
import spray.httpx.SprayJsonSupport._
import io.github.safety_net.galvatron.models.ThreatAssessmentJsonProtocol._

// we don't implement our route structure directly in the service actor because
// we want to be able to test it independently, without having to spin up an actor
class ThreatAssessmentServiceActor extends Actor with ThreatAssessmentService {

  // the HttpService trait defines only one abstract member, which
  // connects the services environment to the enclosing actor or test
  def actorRefFactory = context

  // this actor only runs our route, but you could add
  // other things here, like request stream processing
  // or timeout handling
  def receive = runRoute(myRoute)
}

// this trait defines our service behavior independently from the service actor
trait ThreatAssessmentService extends HttpService {

  val myRoute =
    path("threatAssessments") {
      get {
        parameter("twitterUsername") { username =>
          respondWithMediaType(`application/json`) {
            complete {
              ThreatAssessment(twitterUsername = username, isBot = true)
            }
          }
        }
      }
    }
}