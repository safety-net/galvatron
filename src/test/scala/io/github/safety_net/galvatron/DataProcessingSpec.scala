package io.github.safety_net.galvatron

import org.specs2.mutable.Specification

class DataProcessingSpec extends Specification{

  "dotProduct" should {
    "return 6" in {
      val a = Array.ofDim[Double](1,3)
      a(0)(0) = 1
      a(0)(1) = 2
      a(0)(2) = 1

      val b = Array.ofDim[Double](1,3)
      b(0)(0) = 1
      b(0)(1) = 2
      b(0)(2) = 1

      val result = DataProcessing.dotProduct(a,b)
      println("Dot product = %f\n".format(result))
      result shouldEqual(6)
    }
  }

  "normalEquation" should {
    "return True" in {
      // Vanessa_Daviss example stats
      DataProcessing.normalEquation(dupeTweets = 21.000000,recipPercent = 1.208909) must beTrue
    }
  }
}
