package io.github.safety_net.galvatron

import org.apache.spark.{SparkConf, SparkContext}

class BotDB() {

  val conf = new SparkConf()
    .setMaster("local")
    .setAppName("galvatron")

  val sCtx = new SparkContext(conf)
  val distfile = sCtx.textFile("test.txt")

}
