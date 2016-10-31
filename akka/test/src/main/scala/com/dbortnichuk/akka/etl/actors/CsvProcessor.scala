package com.dbortnichuk.akka.etl.actors

import java.io.File
import java.util.UUID

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import com.dbortnichuk.akka.etl.actors.DbWriter.Line

/**
  * Created by dbort on 28.10.2016.
  */
class CsvProcessor(dbWriterSup: ActorRef)
  extends Actor with ActorLogging with CsvParsing {

  import CsvProcessor._

  def receive = {
    case csv: CsvFile => {
      val csvLines = processCSV(csv)
      csvLines.foreach(dbWriterSup ! _)
    }
  }

  private def processCSV(csv: CsvFile): List[Line] = {
    val csvLines = if(csv.header) csv.lines.tail else csv.lines
    csvLines.map(_.split(csv.delim)).map{values =>
      val userId = values(0).toInt
      val movieId = values(1).toInt
      val rating = values(2).toDouble
      val timestamp = values(3).toInt
      Line(userId, movieId, rating, timestamp)
    }
  }
}

object CsvProcessor {
  def props(dbWriterSup: ActorRef) =
    Props(new CsvProcessor(dbWriterSup))

  //def name = s"csv_processor_${UUID.randomUUID.toString}"
  def name = "csv_processor"

  // represents a new log file
  case class CsvFile(lines: List[String], header: Boolean = true, delim: String = ",")

}

trait CsvParsing {

  import DbWriter._

  // Parses log files. creates line objects from the lines in the log file.
  // If the file is corrupt a CorruptedFileException is thrown
  def parse(file: File): Vector[Line] = {
    // implement parser here, now just return dummy value
    Vector.empty[Line]
  }
}
