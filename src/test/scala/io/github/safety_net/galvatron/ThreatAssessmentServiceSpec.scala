package io.github.safety_net.galvatron

import org.specs2.mutable.Specification
import spray.testkit.Specs2RouteTest
import spray.http._
import StatusCodes._

class ThreatAssessmentServiceSpec extends Specification with Specs2RouteTest with ThreatAssessmentService {
  def actorRefFactory = system

  "ThreatAssessmentService" should {

    "return a greeting for GET requests to the root path" in {
      Get("/threatAssessments?twitterUsername=james") ~> myRoute ~> check {
        responseAs[String] shouldEqual "{\n  \"twitterUsername\": \"james\",\n  \"isBot\": true\n}"
      }
    }

    "leave GET requests to other paths unhandled" in {
      Get("/kermit") ~> myRoute ~> check {
        handled must beFalse
      }
    }

    "return a MethodNotAllowed error for PUT requests to the root path" in {
      Put("/threatAssessments") ~> sealRoute(myRoute) ~> check {
        status === MethodNotAllowed
        responseAs[String] === "HTTP method not allowed, supported methods: GET"
      }
    }
  }
}