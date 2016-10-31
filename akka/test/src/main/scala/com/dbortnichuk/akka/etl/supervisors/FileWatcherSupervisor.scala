package com.dbortnichuk.akka.etl.supervisors

import java.io.FileNotFoundException

import akka.actor.{Actor, ActorLogging, ActorRef, OneForOneStrategy, Props, SupervisorStrategy, Terminated}
import akka.actor.Actor.Receive
import akka.actor.SupervisorStrategy.{Restart, Resume, Stop}
import com.dbortnichuk.akka.etl.actors.{CsvProcessor, FileWatcher}
import com.dbortnichuk.akka.etl.actors.FileWatcher.NewFile

import scala.io.Source
import scala.util.Try

/**
  * Created by dbort on 28.10.2016.
  */
class FileWatcherSupervisor(fileWatcherProps: Props) extends Actor with ActorLogging {

  import com.dbortnichuk.akka.etl.actors.Utils._

  private val fileWatcher = context.system.actorOf(fileWatcherProps, FileWatcher.name)

  context.watch(fileWatcher)


  override def receive: Receive = {
    case nf: NewFile => fileWatcher forward nf
    case Terminated(actorRef) => {
      if (actorRef == fileWatcher){
        context.system.shutdown()
      }
    }
  }

  override def supervisorStrategy: SupervisorStrategy = OneForOneStrategy() {
    case _: FileNotFoundException => Restart
    case _: CorruptedFileException => Resume
    case _: DbBrokenConnectionException => Restart
    case _: DiskError => Stop
  }
}

object FileWatcherSupervisor {

  def props(fileWatcherProps: Props) = Props(new FileWatcherSupervisor(fileWatcherProps))

  def name = "file-watcher-sup"

}
