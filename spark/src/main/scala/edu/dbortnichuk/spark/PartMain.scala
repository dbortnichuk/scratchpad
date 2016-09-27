package edu.dbortnichuk.spark

import java.sql.{DriverManager, ResultSet}

import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by dbort on 22.09.2016.
  */
object PartMain {

  def main(args: Array[String]): Unit = {

    val sparkConf = new SparkConf()
    //    sparkConf.setAppName("test")
    //    sparkConf.setMaster("local[*]")
    val sc = new SparkContext(sparkConf)

    val textFilesRDD = sc.wholeTextFiles(args(0), 3).mapPartitionsWithIndex { (index, iter) =>
      val lines = iter.toSeq
      val connection = JDBCMain.createConnection()
      val st = connection.createStatement()
      val rs = st.executeQuery("SELECT * FROM test.accounts where id = 1")
      val dbName = rs.getString(2)
      lines.map(line => (index, dbName, line._2)).iterator
    }

    textFilesRDD.foreach(println)

  }

}
