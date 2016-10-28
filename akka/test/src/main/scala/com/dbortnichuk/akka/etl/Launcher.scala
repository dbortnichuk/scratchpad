package com.dbortnichuk.akka.etl

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



    val files = getListOfFiles("D:\\samples\\movies\\data")

    val system = ActorSystem("csv-processing")

    val writerProps = DbWriter.props("jdbc:mysql://localhost:3306/test", "root", "admin")
    val writerSupProps = DBWriterSupervisor.props(writerProps)
    val writerSup = system.actorOf(writerSupProps, DBWriterSupervisor.name)


    val csvProcessorProps = CsvProcessor.props
    val csvProcessorSupProps = CsvProcessorSupervisor.props(csvProcessorProps, writerSup)
    val proccessorSup = system.actorOf(csvProcessorSupProps, CsvProcessorSupervisor.name)

    val fileWatcherProps = FileWatcher.props
    val fileWatcherPropsProps = FileWatcherSupervisor.props(fileWatcherProps, proccessorSup)


    //files.map(NewFile(_, 0)).foreach(fileWatcherSupervisor ! _)

  }




}







