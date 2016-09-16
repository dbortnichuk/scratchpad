package edu.dbortnichuk.spark

import java.io.InputStream
import java.net.URL

import org.apache.spark.{SparkConf, SparkContext}

import scala.io.Source

/**
  * Created by dbortnichuk on 28-May-16.
  */
object Main {

  def main(args: Array[String]) {

    val sparkConf = new SparkConf()
    sparkConf.setAppName("test")
    sparkConf.setMaster("local[*]")
    val sc = new SparkContext(sparkConf)

    //val is: InputStream = getClass.getClassLoader.getResourceAsStream("text.txt")
    val filePath: String = getClass.getClassLoader.getResource("text.txt").getPath
    val lines: Iterator[String] = Source.fromFile(filePath).getLines()

    val rdd = sc.parallelize(lines.toSeq)
    rdd.filter(s => s.length < 2).foreach(println)

    sc.stop()

  }

}
