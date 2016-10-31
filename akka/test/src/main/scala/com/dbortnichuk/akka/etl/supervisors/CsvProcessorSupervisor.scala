package com.dbortnichuk.akka.etl.supervisors

import java.io.FileNotFoundException

import akka.actor.{Actor, ActorLogging, ActorRef, OneForOneStrategy, Props, SupervisorStrategy}
import akka.actor.Actor.Receive
import akka.actor.SupervisorStrategy.{Restart, Resume, Stop}
import com.dbortnichuk.akka.etl.actors.CsvProcessor.CsvFile
import com.dbortnichuk.akka.etl.actors.Utils.{CorruptedFileException, DbBrokenConnectionException, DiskError}
import com.dbortnichuk.akka.etl.actors.{CsvProcessor, DbWriter}

/**
  * Created by dbort on 28.10.2016.
  */
class CsvProcessorSupervisor(csvProcessorProps: Props) extends Actor with ActorLogging{

  private val csvProcessor = context.system.actorOf(csvProcessorProps, CsvProcessor.name)

  override def receive = {
    case csv: CsvFile => csvProcessor forward csv
  }

  override def supervisorStrategy: SupervisorStrategy = OneForOneStrategy() {
    case _: FileNotFoundException => Stop
    case _: CorruptedFileException => Resume
    case _: DbBrokenConnectionException => Restart
    case _: DiskError => Stop
  }
}

object CsvProcessorSupervisor {

  def props(csvProcessorProps: Props) = Props(new CsvProcessorSupervisor(csvProcessorProps))

  def name = "csv-processor-sup"

}
