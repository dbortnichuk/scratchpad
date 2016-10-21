package edu.dbortnichuk.spark20

//import org.apache.spark.sql.{DataFrame, RelationalGroupedDataset, SparkSession}


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

//    val sparkSession = SparkSession
//      .builder()
//      .appName("Spark SQL Example")
//      .master("local[*]")
//      .config("spark.sql.warehouse.dir", "file://" + fileHealthCare) // for windows //http://stackoverflow.com/questions/38940312/why-does-spark-report-java-net-urisyntaxexception-relative-path-in-absolute-ur
//      .getOrCreate()
//
//    // For implicit conversions like converting RDDs to DataFrames
//    import sparkSession.implicits._
//
//    //val df = spark.sparkContext.textFile(fileHealthCare).toDF()
//    val sqlContext = sparkSession.sqlContext
//
//    val df = sparkSession.read
//      .format("com.databricks.spark.csv")
//      .option("header", "true") //reading the headers
//      .option("mode", "DROPMALFORMED")
//      .csv(fileHealthCare)
//
////    df.printSchema()
////    println(df.count())
////    print(df.first())
////    println(s"partitions ${df.rdd.getNumPartitions}")
//
//    df.createOrReplaceTempView("hcf")
//
//    val sqlDF = sparkSession.sql("SELECT * FROM hcf")
//    //println(s"sqlDF ${df.count()}")
//
//    val sqlDFDist: DataFrame = sqlDF.distinct().select("Facility Name")
//    //println(s"sqlDFDist ${df.count()}")
//
//    //sqlDFDist.explain()
//    sqlDF.printSchema()
//
//    val groupDS: RelationalGroupedDataset = sqlDF.groupBy("Facility Name")
//    println(groupDS.count())
//
//
//
//    sparkSession.stop()



  }

}
