package com.dbortnichuk.akka.etl.supervisors

import java.io.FileNotFoundException

import akka.actor.{Actor, ActorLogging, OneForOneStrategy, Props, SupervisorStrategy}
import akka.actor.Actor.Receive
import akka.actor.SupervisorStrategy.{Restart, Resume, Stop}
import com.dbortnichuk.akka.etl.actors.CsvProcessor.CsvFile
import com.dbortnichuk.akka.etl.actors.DbWriter
import com.dbortnichuk.akka.etl.actors.DbWriter.Line
import com.dbortnichuk.akka.etl.actors.Utils.{CorruptedFileException, DbBrokenConnectionException, DiskError}

/**
  * Created by dbort on 28.10.2016.
  */
class DBWriterSupervisor(dbWriterProps: Props) extends Actor with ActorLogging{

  private val dbWriter = context.system.actorOf(dbWriterProps, DbWriter.name)

  override def receive = {
    case line: Line => dbWriter forward line
  }

  override def supervisorStrategy: SupervisorStrategy = OneForOneStrategy() {
    case _: FileNotFoundException => Resume
    case _: CorruptedFileException => Resume
    case _: DbBrokenConnectionException => Restart
    case _: DiskError => Stop
  }
}

object DBWriterSupervisor{

  def props(dbWriterProps: Props) = Props(new DBWriterSupervisor(dbWriterProps))

  def name = "db-writer-sup"

}
