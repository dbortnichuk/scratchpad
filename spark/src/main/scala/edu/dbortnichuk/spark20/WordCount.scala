package edu.dbortnichuk.spark20



/**
  * Created by dbort on 12.10.2016.
  */
object WordCount {

  def main(args: Array[String]): Unit = {

    val fileText: String = edu.dbortnichuk.spark.Main.getPathToRes("text.txt")

//    val spark = SparkSession
//      .builder()
//      .appName("Spark SQL Example")
//      .master("local[*]")
//      .config("spark.sql.warehouse.dir", "file://" + fileText) // for windows //http://stackoverflow.com/questions/38940312/why-does-spark-report-java-net-urisyntaxexception-relative-path-in-absolute-ur
//      .getOrCreate()
//
//    import spark.implicits._
//
//    val df = spark.read.text(fileText)
//    //val ds = df.map(row => row.getAs[String](0)).flatMap(s => s.split(" ")).map(s => (s, 1)).groupByKey(kv => kv._1).mapGroups((k, v) => (k, v.length))
//    //val ds = df.map(row => row.getAs[String](0)).flatMap(s => s.split(" ")).map(s => (s, 1)).rdd.reduceByKey(_ + _).toDS()
//    val ds = df.map(_.getAs[String](0)).flatMap(_.split(" ")).map((_, 1)).groupByKey(_._1).mapGroups((w, xs) => (w, xs.length))
//    ds.show()
//    //df.foreach(row => println(row))
//    //println(df.count())
//
//    spark.stop()


  }

}
