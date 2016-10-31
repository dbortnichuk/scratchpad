package com.dbortnichuk.akka.etl.actors

import java.io.File
import java.util.UUID

import akka.actor.{Actor, ActorRef, PoisonPill, Props}
import com.dbortnichuk.akka.etl.actors.CsvProcessor.CsvFile

import scala.io.Source
import scala.util.{Failure, Success, Try}


/**
  * Created by dbort on 28.10.2016.
  */
class FileWatcher(csvProcessorSup: ActorRef) extends Actor with FileWatchingAbilities {

  import FileWatcher._

  def receive = {
    case NewFile(file) =>
      val fileContents = Try {
        val bs = Source.fromFile(file)
        bs.getLines().toList
      }
      fileContents match {
        case Success(lines) => csvProcessorSup ! CsvFile(lines)
        case Failure(t) => throw t
      }
    //csvProcessor ! CsvProcessor.CsvFile(file)
    //    case SourceAbandoned(uri) if uri == source =>
    //      self ! PoisonPill
  }

  @scala.throws[Exception](classOf[Exception])
  override def preStart(): Unit = {
    super.preStart()
    println("preStart")
  }

  @scala.throws[Exception](classOf[Exception])
  override def postStop(): Unit = {
    super.postStop()
    println("postStop")
  }

  @scala.throws[Exception](classOf[Exception])
  override def preRestart(reason: Throwable, message: Option[Any]): Unit = {
    super.preRestart(reason, message)
    println("preRestart")
  }

  @scala.throws[Exception](classOf[Exception])
  override def postRestart(reason: Throwable): Unit = {
    super.postRestart(reason)
    println("postRestart")
  }
}

object FileWatcher {
  def props(csvProcessorSup: ActorRef) = Props(new FileWatcher(csvProcessorSup))

  //def name = s"file-watcher-${UUID.randomUUID.toString}"
  def name = "file-watcher"

  case class NewFile(file: File)

  //case class SourceAbandoned(uri: String)

}

trait FileWatchingAbilities {
  def register(uri: String) {

  }
}
