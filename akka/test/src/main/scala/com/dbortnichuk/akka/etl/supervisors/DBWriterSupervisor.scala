package com.dbortnichuk.akka.etl.supervisors

import akka.actor.{Actor, Props}
import akka.actor.Actor.Receive
import com.dbortnichuk.akka.etl.actors.DbWriter

/**
  * Created by dbort on 28.10.2016.
  */
class DBWriterSupervisor(dbWriterProps: Props) extends Actor{

  private val dbWriter = context.system.actorOf(dbWriterProps, DbWriter.name)

  override def receive: Receive = ???
}

object DBWriterSupervisor{

  def props(dbWriterProps: Props) = Props(new DBWriterSupervisor(dbWriterProps))

  def name = "db-writer-sup"

}
