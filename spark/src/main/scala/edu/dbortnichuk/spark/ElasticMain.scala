package edu.dbortnichuk.spark

import org.apache.spark.{SparkConf, SparkContext}
import org.elasticsearch.spark.rdd.EsSpark

/**
  * Created by dbort on 22.09.2016.
  */
object ElasticMain {

  def main(args: Array[String]): Unit = {

    val sparkConf = new SparkConf()
    sparkConf.setAppName("test")
    sparkConf.setMaster("local[*]")
    val sc = new SparkContext(sparkConf)
    val rdd = sc.parallelize(Seq((1, "some"), (2, "other")))

    //val esParams = Map[String, String]("es.nodes" -> "192.168.159.1", "es.port" -> "2900")
    val esParams = Map[String, String]("es.nodes" -> "localhost", "es.port" -> "9200")
    val esRes = "spark/docs"
    EsSpark.saveToEs(rdd, esRes, esParams)

    sc.stop()


  }

}
