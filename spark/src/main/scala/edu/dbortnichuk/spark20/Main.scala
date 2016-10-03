package edu.dbortnichuk.spark20

import org.apache.spark.sql.SparkSession
import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by dbort on 30.09.2016.
  */
object Main {

  def main(args: Array[String]): Unit = {

//    val sparkConf = new SparkConf()
//    sparkConf.setAppName("test")
//    sparkConf.setMaster("local[*]")
//    val sc = new SparkContext(sparkConf)

    val fileHealthCare: String = edu.dbortnichuk.spark.Main.getPathToRes("Health_Care_Facilities.csv")

    val spark = SparkSession
      .builder()
      .appName("Spark SQL Example")
      .master("local[*]")
      .config("spark.sql.warehouse.dir", "file://" + fileHealthCare) // for windows //http://stackoverflow.com/questions/38940312/why-does-spark-report-java-net-urisyntaxexception-relative-path-in-absolute-ur
      .getOrCreate()

    // For implicit conversions like converting RDDs to DataFrames
    import spark.implicits._

    //val df = spark.sparkContext.textFile(fileHealthCare).toDF()
    val df = spark.read.json(fileHealthCare)
    println(df.count())

    spark.stop()

  }

}
