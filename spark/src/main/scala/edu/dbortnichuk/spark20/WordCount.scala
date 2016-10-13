package edu.dbortnichuk.spark20

import org.apache.spark.sql.SparkSession

/**
  * Created by dbort on 12.10.2016.
  */
object WordCount {

  def main(args: Array[String]): Unit = {

    val fileText: String = edu.dbortnichuk.spark.Main.getPathToRes("text.txt")

    val spark = SparkSession
      .builder()
      .appName("Spark SQL Example")
      .master("local[*]")
      .config("spark.sql.warehouse.dir", "file://" + fileText) // for windows //http://stackoverflow.com/questions/38940312/why-does-spark-report-java-net-urisyntaxexception-relative-path-in-absolute-ur
      .getOrCreate()

    import spark.implicits._

    val df = spark.read.text(fileText)
    //val ds = df.map(row => row.getAs[String](0)).flatMap(s => s.split(" ")).map(s => (s, 1)).groupByKey(kv => kv._1).mapGroups((k, v) => Seq(k, v.length))
    val ds = df.map(row => row.getAs[String](0)).flatMap(s => s.split(" ")).map(s => (s, 1)).rdd.reduceByKey(_ + _).toDS()
    ds.show()
    //df.foreach(row => println(row))
    //println(df.count())

    spark.stop()


  }

}
