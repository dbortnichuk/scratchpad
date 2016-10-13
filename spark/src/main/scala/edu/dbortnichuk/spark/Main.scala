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

    val numbersRDD1 = sc.parallelize(Seq(3, 3, 1, 4))
    val numbersRDD2 = sc.parallelize(Seq(3, 2, 1, 5))
    val numbersRDD3 = sc.parallelize(Seq((4, 9), (3, 7), (6, 7)))
    //val numbersRDD2 = sc.textFile(fileNumbersPath, 2).map(_.toInt)
    val wordsRDD = sc.textFile(fileWordsPath, 2)
    val textRDD = sc.textFile(fileTextPath)


    //passing functions
    //SearchFunctions("potato").getMatchesFieldReference(wordsRDD).foreach(println) //not serializable
    //SearchFunctions("potato").getMatchesNoReference(wordsRDD).foreach(println) //ok, serializable

    //word count
    textRDD.flatMap(line => line.split(" ")).map(w => (w, 1)).reduceByKey(_ + _).foreach(t => println(t._1 + " " + t._2))

    //val numbersRDDSample = numbersRDD1.sample(withReplacement = false, 0.2)//takes a fraction 0 - 2 elems out of 5

    val unitedRDD = wordsRDD.union(numbersRDD1.map(n => n.toString))
    val cartesianRDD = wordsRDD.cartesian(numbersRDD1.map(n => n.toString)).collect()

    val sumRDD = numbersRDD1.reduce((p, n) => p + n)
    val anotherSumRDD = numbersRDD1.fold(0)((p, n) => p + n)

    val aggRDD = numbersRDD1.aggregate((0, 0))(//zero values for sum and number of values per partition
      (acc, value) => (acc._1 + value, acc._2 + 1), //first - sum of numbers per partition, second - number of values per partition
      (acc1, acc2) => (acc1._1 + acc2._1, acc1._2 + acc2._2) //first - sum of numbers per rdd, second - number of values per rdd
    )

    //val avg = aggRDD._1 / aggRDD._2.toDouble // count sum of numbers div number of numbers eg - avg value

    //val topWords = wordsRDD.top(2).foreach(println)
    val numbersPairRDD = numbersRDD1.zip(numbersRDD2)

    val reducedRDD = numbersPairRDD.reduce((p, n) => (p._1 + n._1, p._2 + n._2))
    val reducedKeyRDD = numbersPairRDD.reduceByKey((p, n) => p + n)
    val mappedValRDD = numbersPairRDD.mapValues(_.toString)
    val flatMappedValRDD = numbersPairRDD.flatMapValues(v => Seq(v, v + 1))
    val sortedPairRDD = numbersPairRDD.sortByKey()
    val joinedRDD = numbersPairRDD.join(numbersRDD3)
    val joinedLeftRDD = numbersPairRDD.leftOuterJoin(numbersRDD3)
    //val cogroupedRDD = numbersRDD1.com


    val stats = numbersRDD1.stats().mean
    println("mean: " + stats)
//    joinedRDD.foreach(print)
//
//    joinedLeftRDD.foreach(print)


    //flatMappedValRDD.foreach(println)



    //reducedKeyRDD.foreach(println)
    //println(reducedKeyRDD)



    sc.stop()

  }

}
