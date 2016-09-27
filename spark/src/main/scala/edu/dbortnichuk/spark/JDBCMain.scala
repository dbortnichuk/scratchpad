package edu.dbortnichuk.spark

import java.sql.{DriverManager, ResultSet}

import org.apache.spark.rdd.JdbcRDD
import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by dbort on 22.09.2016.
  */
object JDBCMain {

  def main(args: Array[String]): Unit = {

    val sparkConf = new SparkConf()
    //    sparkConf.setAppName("test")
    //    sparkConf.setMaster("local[*]")
    val sc = new SparkContext(sparkConf)


    val textFilesRDD = sc.wholeTextFiles(args(0))

    val data = new JdbcRDD(sc,
      createConnection, "SELECT * FROM test.accounts where id >= ? and id <= ?",
      lowerBound = 1, upperBound = 3, numPartitions = 2, mapRow = extractValues)

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
