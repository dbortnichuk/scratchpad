package edu.dbortnichuk.spark


import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by dbort on 23.09.2016.
  */
object SQLMain {

  //spark-submit --class edu.dbortnichuk.spark.SQLMain --master local[*] --name SQLTest jars/spark-1.0-SNAPSHOT.jar  hdfs:///user/cloudera/files/json/ hdfs:///user/cloudera/files/parq/
//  def main(args: Array[String]): Unit = {
//
//    val sparkConf = new SparkConf()
//    sparkConf.setAppName("test")
//    sparkConf.setMaster("local[*]")
//    val sc = new SparkContext(sparkConf)
//    val hiveCtx = new HiveContext(sc)
//
//    val input = hiveCtx.read.json(args(0))
//    input.registerTempTable("tweets")
//    val topTweets = hiveCtx.sql("SELECT text, retweetCount FROM tweets ORDER BY retweetCount LIMIT 10")
//
//    input.printSchema()
//    topTweets.write.parquet(args(1))
//    //topTweets.foreach(println)
//
//    sc.stop()

//  }

}
