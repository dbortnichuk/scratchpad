package com.dbortnichuk.akka.etl.actors

import java.util.UUID

import akka.actor.{Actor, Props}

import scala.util.Try
import com.dbortnichuk.akka.etl.actors.Utils._

/**
  * Created by dbort on 28.10.2016.
  */
class DbWriter(databaseUrl: String, userName: String, password: String) extends Actor {

  val connectionTry = getDBConnection(databaseUrl, userName, password)

  import DbWriter._

  def receive = {
    case Line(userId: Int, movieId: Int, rating: Double, timestamp: Long) =>
      val operationTry = connectionTry.flatMap { conn =>
        Try {
          val statement = conn.prepareStatement("INSERT INTO ratings (userId, movieId, rating, timestamp) VALUES (?, ?, ?, ?)")
          statement.setInt(1, userId)
          statement.setInt(2, movieId)
          statement.setDouble(3, rating)
          statement.setLong(4, timestamp)
          statement.executeUpdate()
          //rs.next()
        }
      }
      operationTry.failed.foreach(t => throw t)
  }

  override def postStop(): Unit = {
    connectionTry.foreach(_.close())
  }
}

object DbWriter {
  def props(databaseUrl: String, userName: String, password: String) =
    Props(new DbWriter(databaseUrl, userName, password))

  def name = s"""db-writer-${UUID.randomUUID.toString}""""

  // A line in the log file parsed by the LogProcessor Actor
  case class Line(userId: Int, movieId: Int, rating: Double, timestamp: Long)

}
