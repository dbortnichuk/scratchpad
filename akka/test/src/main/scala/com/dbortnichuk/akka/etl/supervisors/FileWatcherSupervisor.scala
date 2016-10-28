package com.dbortnichuk.akka.etl.supervisors

import akka.actor.{Actor, ActorRef, Props}
import akka.actor.Actor.Receive
import com.dbortnichuk.akka.etl.actors.CsvProcessor

/**
  * Created by dbort on 28.10.2016.
  */
class FileWatcherSupervisor(fileWatcherProps: Props, logProcessingSup: ActorRef) extends Actor {

  private val csvProcessor = context.system.actorOf(fileWatcherProps, CsvProcessor.name)

  override def receive: Receive = ???
}

object FileWatcherSupervisor {

  def props(fileWatcherProps: Props, logProcessingSup: ActorRef) = Props(new FileWatcherSupervisor(fileWatcherProps, logProcessingSup))

  def name = "file-watcher-sup"

}
