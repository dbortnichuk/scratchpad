package com.dbortnichuk.akka.etl

import java.io.File

import akka.actor.{ActorSystem, Props}
import com.dbortnichuk.akka.etl.actors.FileWatcher.NewFile
import com.dbortnichuk.akka.etl.actors.{CsvProcessor, DbWriter, FileWatcher}
import com.dbortnichuk.akka.etl.actors.Utils._
import com.dbortnichuk.akka.etl.supervisors.{CsvProcessorSupervisor, DBWriterSupervisor, FileWatcherSupervisor}

/**
  * Created by dbort on 28.10.2016.
  */
object Launcher {

  def main(args: Array[String]): Unit = {

    val files = getListOfFiles("D:\\samples\\movies\\data\\small1")

    val system = ActorSystem("csv-processing")

    val writerProps = DbWriter.props("jdbc:mysql://localhost:3306/test", "root", "admin")
    val writerSupProps = DBWriterSupervisor.props(writerProps)
    val writerSup = system.actorOf(writerSupProps, DBWriterSupervisor.name)


    val csvProcessorProps = CsvProcessor.props(writerSup)
    val csvProcessorSupProps = CsvProcessorSupervisor.props(csvProcessorProps)
    val processorSup = system.actorOf(csvProcessorSupProps, CsvProcessorSupervisor.name)

    val fileWatcherProps = FileWatcher.props(processorSup)
    val fileWatcherSupProps = FileWatcherSupervisor.props(fileWatcherProps)
    val watcherSup = system.actorOf(fileWatcherSupProps, FileWatcherSupervisor.name)
    watcherSup ! NewFile(new File("D:\\samples\\movies\\data\\small\\ratings1.csv"))
    watcherSup ! NewFile(new File("D:\\samples\\movies\\data\\small1"))

    //files.map(f => NewFile(f)).foreach(watcherSup ! _)
    import scala.concurrent.duration._
    system.awaitTermination(600 seconds)

  }


}







