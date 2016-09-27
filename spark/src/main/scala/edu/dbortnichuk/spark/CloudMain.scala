package edu.dbortnichuk.spark

import java.sql.{DriverManager, ResultSet}

import org.apache.hadoop.io.compress.BZip2Codec
import org.apache.spark.rdd.JdbcRDD
import org.apache.spark.storage.StorageLevel
import org.apache.spark.{HashPartitioner, SparkConf, SparkContext}

import scala.io.Source

/**
  * Created by dbort on 19.09.2016.
  */
object CloudMain {

  def getPathToRes(name: String): String = getClass.getClassLoader.getResource(name).getPath

  def main(args: Array[String]): Unit = {

    val sparkConf = new SparkConf()
    //    sparkConf.setAppName("test")
    //    sparkConf.setMaster("local[*]")
    val sc = new SparkContext(sparkConf)
    //sc.setCheckpointDir("hdfs:///user/cloudera/spark/checkpoints/")


    //    val seqRDD = sc.sequenceFile[Int, String]("hdfs:///user/cloudera/files/seq/sample").partitionBy(new HashPartitioner(3)).persist()
    //    seqRDD.foreach(println)

    //val textFilesRDD = sc.wholeTextFiles("hdfs:///user/cloudera/files/xml/*.xml")

    val textFilesRDD = sc.wholeTextFiles(args(0), 3)

    val asdasd = "".intern()

    //textFilesRDD.saveAsTextFile(args(1))
    //textFilesRDD.saveAsTextFile(args(1), classOf[BZip2Codec])

    //textFilesRDD.saveAsSequenceFile(args(1))

//    val blankLines = sc.accumulator(0) // Create an Accumulator[Int] initialized to 0
//    val callSigns = textFilesRDD.map { file => {
//      val content = file._2
//      content.split("\n").flatMap {
//        case ln if !ln.isEmpty => Some(ln)
//        case _ =>
//          blankLines += 1
//          None
//      }
//    }
//    }
//
//    callSigns.map(arr => arr.mkString("\n")).saveAsTextFile(args(1))
    //println("blanks: " + blankLines.value)


    //println(data.collect().toList)


    // println("partitions: " + seqRDD.getNumPartitions)
    //Thread.sleep(3000)

    //val wordsRDD = sc.textFile(args(0))
    //wordsRDD.persist(StorageLevel.DISK_ONLY)
    //    wordsRDD.flatMap(line => line.split(" ")).map(w => (w, 1)).reduceByKey(_ + _).saveAsTextFile(args(1)) //foreach(t => println(t._1 + " " + t._2))
    //    println(wordsRDD.getNumPartitions)

    sc.stop()

  }

  def createConnection() = {
    Class.forName("com.mysql.jdbc.Driver").newInstance()
    DriverManager.getConnection("jdbc:mysql://192.168.159.1:3306/test", "username", "password")
  }

  def extractValues(r: ResultSet) = {
    (r.getInt(1), r.getString(2))
  }

}

case class Data(first: String, second: String)
