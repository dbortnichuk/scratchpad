package com.dbortnichuk.akka.etl.actors

import java.io.File
import java.util.UUID

import akka.actor.{Actor, ActorRef, PoisonPill, Props}


/**
  * Created by dbort on 28.10.2016.
  */
class FileWatcher extends Actor with FileWatchingAbilities {

  import FileWatcher._

  def receive = {
    case NewFile(file, _) =>
      csvProcessor ! CsvProcessor.CsvFile(file)
    case SourceAbandoned(uri) if uri == source =>
      self ! PoisonPill
  }
}

object FileWatcher {
  def props = Props(new FileWatcher())

  def name = s"file-watcher-${UUID.randomUUID.toString}"

  //case class NewFile(file: File, timeAdded: Long)

  case class SourceAbandoned(uri: String)

}

trait FileWatchingAbilities {
  def register(uri: String) {

  }
}
