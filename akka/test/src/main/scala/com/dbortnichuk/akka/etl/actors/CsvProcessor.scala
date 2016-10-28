package com.dbortnichuk.akka.etl.actors

import java.io.File
import java.util.UUID

import akka.actor.{Actor, ActorLogging, ActorRef, Props}

/**
  * Created by dbort on 28.10.2016.
  */
class CsvProcessor
  extends Actor with ActorLogging with CsvParsing {

  import CsvProcessor._

  def receive = {
    case CsvFile(file) =>
      val lines: Vector[DbWriter.Line] = parse(file)
      lines.foreach(dbWriter ! _)
  }
}

object CsvProcessor {
  def props =
    Props(new CsvProcessor())

  def name = s"csv_processor_${UUID.randomUUID.toString}"

  // represents a new log file
  case class CsvFile(file: File)

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
