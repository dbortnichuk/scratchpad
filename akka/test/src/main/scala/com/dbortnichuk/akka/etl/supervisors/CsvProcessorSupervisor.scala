package com.dbortnichuk.akka.etl.supervisors

import akka.actor.{Actor, ActorRef, Props}
import akka.actor.Actor.Receive
import com.dbortnichuk.akka.etl.actors.{CsvProcessor, DbWriter}

/**
  * Created by dbort on 28.10.2016.
  */
class CsvProcessorSupervisor(csvProcessorProps: Props, dbWriterSup: ActorRef) extends Actor{

  private val csvProcessor = context.system.actorOf(csvProcessorProps, CsvProcessor.name)

  override def receive = {

  }
}

object CsvProcessorSupervisor {

  def props(csvProcessorProps: Props, dbWriterSup: ActorRef) = Props(new CsvProcessorSupervisor(csvProcessorProps, dbWriterSup))

  def name = "csv-processor-sup"

}
