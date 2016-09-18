package edu.dbortnichuk.spark

import java.io.InputStream
import java.net.URL

import org.apache.spark.{SparkConf, SparkContext}

import scala.io.Source

/**
  * Created by dbortnichuk on 28-May-16.
  */
object Main {

  def getPathToRes(name: String): String = getClass.getClassLoader.getResource(name).getPath

  def main(args: Array[String]) {

    val sparkConf = new SparkConf()
    sparkConf.setAppName("test")
    sparkConf.setMaster("local[*]")
    val sc = new SparkContext(sparkConf)

    val fileNumbersPath: String = getPathToRes("numbers.txt")
    val fileWordsPath: String = getPathToRes("words.txt")
    val fileTextPath: String = getPathToRes("text.txt")

    //val numbersLines: Iterator[String] = Source.fromFile(fileNumbersPath).getLines()

    val numbersRDD = sc.parallelize(Seq(1, 2, 3, 4))
    val wordsRDD = sc.textFile(fileWordsPath, 2)
    val textRDD = sc.textFile(fileTextPath, 2)

    //passing functions
    //SearchFunctions("potato").getMatchesFieldReference(wordsRDD).foreach(println) //not serializable
    //SearchFunctions("potato").getMatchesNoReference(wordsRDD).foreach(println) //ok, serializable

    //word count
    //textRDD.flatMap(line => line.split(" ")).map(w => (w, 1)).reduceByKey(_ + _).foreach(t => println(t._1 + " " + t._2))

    //val numbersRDDSample = numbersRDD.sample(withReplacement = false, 0.2)//takes a fraction 0 - 2 elems out of 5

    val unitedRDD = wordsRDD.union(numbersRDD.map(n => n.toString))
    val cartesianRDD = wordsRDD.cartesian(numbersRDD.map(n => n.toString)).collect()

    val sumRDD = numbersRDD.reduce((p, n) => p + n)
    val anotherSumRDD = numbersRDD.fold(0)((p, n) => p + n)

    val aggRDD = numbersRDD.aggregate((0, 0))( //zero values for sum and number of values per partition
      (acc, value) => (acc._1 + value, acc._2 + 1), //first - sum of numbers per partition, second - number of values per partition
      (acc1, acc2) => (acc1._1 + acc2._1, acc1._2 + acc2._2) //first - sum of numbers per rdd, second - number of values per rdd
    )

    //val avg = aggRDD._1 / aggRDD._2.toDouble // count sum of numbers div number of numbers eg - avg value

    val topWords = wordsRDD.top(2)


    sc.stop()

  }

}
