package com.dbortnichuk.akka.etl.actors

import java.io.File
import java.sql.{Connection, DriverManager}

import scala.util.Try

/**
  * Created by dbort on 28.10.2016.
  */
object Utils {

  def getListOfFiles(dir: String): List[File] = {
    val d = new File(dir)
    if (d.exists && d.isDirectory) {
      d.listFiles.filter(_.isFile).toList
    } else {
      List[File]()
    }
  }

  def getListOfFileNames(dir: String): List[String] = {
    getListOfFiles(dir).map(_.getAbsolutePath)
  }

  def getDBConnection(url: String, userName: String, password: String): Try[Connection] = {
    Try {
      Class.forName("com.mysql.jdbc.Driver")
      DriverManager.getConnection(url, userName, password)
    }
  }

  //val fileNames = getListOfFileNames("D:\\samples\\movies\\data").foreach(println)

  //    val connectionTry = getDBConnection("jdbc:mysql://localhost:3306/test", "root", "admin")
  //    val operationsTry = connectionTry.flatMap { connection =>
  //      Try {
  //        val statement = connection.createStatement
  //        val rs = statement.executeQuery("SELECT * FROM accounts")
  //        //rs.next()
  //        rs.getString("name")
  //      }
  //    }
  //
  //    operationsTry.foreach(println)
  //
  //    operationsTry.failed foreach { e =>
  //      //e.printStackTrace()
  //      print("error")
  //    }
  //
  //    connectionTry.foreach {
  //      _.close
  //    }

}
