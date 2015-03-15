package io.github.safety_net.galvatron

import java.io.{File, PrintWriter}
import scala.collection.mutable.ListBuffer
import scala.io.Source

object GetTrainingData {

  //Bots examples
  val bots: List[String] = Source.fromFile("src/test/resources/bots.list").getLines().toList

  //Non Bot examples
  val notBots: List[String] = Source.fromFile("src/test/resources/notbots.list").getLines().toList

  val bt = {
    // file with twitterKeys not to be committed!!
    // structure
    // ConsumerKey \n
    // ConsumerSecret \n
    // AccessToken \n
    // AccessTokenSecret
    val filename = "src/test/resources/twitterKeys.keys"
    val keyLines: List[String] = Source.fromFile(filename).getLines().toList
    new BotTests(keyLines(0), keyLines(1), keyLines(2), keyLines(3))
  }

  val compound: ListBuffer[List[Double]] = ListBuffer()

  for (botname: String <- bots){
    compound.append(List[Double](bt.maxDupeTweetCount(botname),bt.reciprocationPercentage(botname),1))
  }

  for (name: String <- notBots){
    compound.append(List[Double](bt.maxDupeTweetCount(name),bt.reciprocationPercentage(name),0))
  }

  val x = new File("src/test/resources/trainingXVals.dat")
  val y = new File("src/test/resources/trainingYVals.dat")
  if(x.isFile() && y.isFile()) {
    // Should overwrite previous contents
    val xwriter = new PrintWriter(x)
    val ywriter = new PrintWriter(y)
    for (entry: List[Double] <- compound) {
      // Tab separated is preferable for octave to read in

      // x values are the feature matrix
      xwriter.write("  %f  %f".format(entry(0), entry(1)))
      xwriter.println()

      // y values are for the label, in this case the boolean Bot or not
      ywriter.write("  %f".format(entry(2)))
      ywriter.println()
    }
    xwriter.close()
    ywriter.close()
  }
  else
    println("not a file")
    println(x.getAbsolutePath)
    println(y.getAbsolutePath)
}
