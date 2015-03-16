package io.github.safety_net.galvatron

object DataProcessing {

  def normalEquation(dupeTweets : Double, recipPercent: Double ) : Boolean = {
    // Using the normal equation to compute
    //
    // B = (x'*x)\x'*y
    // Computed from octave
    val theta_normal = Array.ofDim[Double](1,3)
    theta_normal(0)(0) = 0.3307635
    theta_normal(0)(1) = 0.0323227
    theta_normal(0)(2) = -0.0054519

    // prediction = A.B
    // where A is the matrix of features to predict on
    // And B is the OLS predictor matrix computed from Octave
    val currentUser = Array.ofDim[Double](1,3)
    currentUser(0)(0) = 1
    currentUser(0)(1) = dupeTweets
    currentUser(0)(2) = recipPercent

    val result = dotProduct(theta_normal,currentUser)

    if(result < 0.5)
      false
    else
      true
  }

  def dotProduct(a : Array[Array[Double]],b : Array[Array[Double]]) : Double = {
    if(a.length != 1 || b.length !=1 || a(0).length != 3 || b(0).length != 3)
      throw new NumberFormatException("Array size mismatch")

    // multiply each dimension and sum (dot product of a vector)
    a(0).zipWithIndex.map{ case (s,i) =>  b(0)(i)*s }.sum
  }
}
