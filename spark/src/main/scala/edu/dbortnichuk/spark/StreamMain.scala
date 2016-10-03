package edu.dbortnichuk.spark

import java.sql.DriverManager

import kafka.serializer.{StringDecoder, StringEncoder}
import org.apache.hadoop.io.Text
import org.apache.hadoop.mapred.SequenceFileOutputFormat
import org.apache.spark.{Partitioner, SparkConf, SparkContext}
import org.apache.spark.streaming.dstream.{DStream, InputDStream}
import org.apache.spark.streaming.kafka.KafkaUtils
import org.apache.spark.streaming.{Seconds, StreamingContext}

/**
  * Created by dbort on 26.09.2016.
  */
object StreamMain {

  //spark-submit --class edu.dbortnichuk.spark.StreamMain --master local[*] --name StreamTest jars/spark-1.0-SNAPSHOT.jar  hdfs:///user/cloudera/files/json/
  //export SPARK_JAVA_OPTS=-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005

  def main(args: Array[String]): Unit = {

    def createStreamingContext() = {
      val conf = new SparkConf()
      conf.setAppName("StreamingLogInput")
      conf.setMaster("local[*]")
      val sc = new SparkContext(conf)
      // Create a StreamingContext with a 1 second batch size
      val ssc = new StreamingContext(sc, Seconds(1))
      ssc.checkpoint("hdfs:///user/cloudera/spark")
      ssc
    }

//    val conf = new SparkConf()
//    conf.setAppName("StreamingLogInput")
//    conf.setMaster("local[*]")
    // Create a StreamingContext with a 1 second batch size
    //val ssc = new StreamingContext(conf, Seconds(3))
    val ssc = StreamingContext.getOrCreate("hdfs:///user/cloudera/spark", createStreamingContext _ )

    // Create a DStream from all the input on port 7777
    //    val lines = ssc.socketTextStream("192.168.159.226", 7777)
    //    val errorLines = processLines(lines)
    //    // Print out the lines with errors, which causes this DStream to be evaluated
    //    errorLines.print()
    // start our streaming context and wait for it to "finish"

    //Thread.sleep(5000)

    //ssc.checkpoint("hdfs:///user/cloudera/spark")

    val fileStream = ssc.textFileStream(args(0))
    fileStream.print()
    fileStream.foreachRDD{rdd =>
      rdd.saveAsTextFile(args(1))
    }


//    val kafkaParams = Map[String, String]("metadata.broker.list" -> "192.168.159.226:9092", "serializer.class" -> classOf[StringEncoder].getName, "auto.offset.reset" -> "largest")
//    val kafkaStream: InputDStream[(String, String)] = KafkaUtils.createDirectStream[String, String, StringDecoder, StringDecoder](
//      ssc,
//      kafkaParams,
//      Set("test"))
//    val kafkaWindow = kafkaStream.window(Seconds(args(1).toLong), Seconds(args(2).toLong)) //.map(keyVal => (new Text(keyVal._1), new Text(keyVal._2))) //.foreachRDD(rdd => println(rdd.collect().length))
//    //val kafkaWindow = kafkaStream.window(Seconds(args(1).toLong), Seconds(args(2).toLong)).updateStateByKey(updateRunningSum _)
//    kafkaWindow.print()
//
//    kafkaWindow.foreachRDD { rdd =>
//      rdd.foreachPartition { it =>
//        Class.forName("com.mysql.jdbc.Driver").newInstance()
//        val conn = DriverManager.getConnection("jdbc:mysql://192.168.159.1:3306/test", "username", "password")
//        val insert = conn.prepareStatement("INSERT INTO keyval (id) VALUES (?); ")
//        it.foreach { tup =>
//          insert.setInt(1, tup._1.toInt)
//          //insert.setString(2, "some")
//          insert.execute()
//        }
//        conn.close()
//      }
//    }
    //kafkaWindow.print()
    //kafkaWindow.saveAsTextFiles("hdfs:///user/cloudera/spark/out/outputDir", "txt")
    //kafkaWindow.saveAsHadoopFiles[SequenceFileOutputFormat[Text, Text]]("hdfs:///user/cloudera/spark/out/outputDir", "seq")


    //    val kafkaStream1: InputDStream[(String, String)] = KafkaUtils.createDirectStream[String, String, StringDecoder, StringDecoder](
    //      ssc,
    //      kafkaParams,
    //      Set("test1"))
    //
    //    val kafkaUnif = ssc.union(Seq(kafkaStream, kafkaStream1)).groupByKey()
    //
    //    kafkaUnif.print()


    //val mapped: DStream[String] = kafkaStream.flatMap(_._2.split(",")).filter(_.length < 15)
    //val reduced = kafkaStream.reduceByKey((p, n) => p + " delim " + n)
    //    val grouped = kafkaStream.groupByKey(new ExtractPartitioner(2)).foreachRDD { rdd =>
    //      //rdd.partitions.foreach(p => println("partition: " + p.index))
    //      rdd.foreachPartition { it => it.foreach(println) }
    //    }

    //grouped.print()

    ssc.start()

    ssc.awaitTermination()
    ssc.stop()
  }

  def updateRunningSum(values: Seq[String], state: Option[String]) = {
    Some(state.getOrElse("") + values.mkString(" | "))
  }

  def processLines(lines: DStream[String]) = {
    // Filter our DStream for lines with "error"
    lines.filter(_.contains("error"))
  }

}

class ExtractPartitioner(partitions: Int) extends Partitioner {
  override def numPartitions: Int = partitions

  override def getPartition(key: Any): Int = {
    val k = key.asInstanceOf[String].toInt
    k % partitions
  }
}
