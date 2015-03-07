package io.github.safety_net.galvatron.models

import spray.json.DefaultJsonProtocol

object ThreatAssessmentJsonProtocol extends DefaultJsonProtocol {
  implicit val threatAssessmentFormat = jsonFormat2(ThreatAssessment)
}

case class ThreatAssessment(twitterUsername: String, isBot: Boolean)
