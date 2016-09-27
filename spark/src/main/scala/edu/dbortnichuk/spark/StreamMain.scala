package edu.dbortnichuk.spark

import kafka.serializer.{StringDecoder, StringEncoder}
import org.apache.spark.SparkConf
import org.apache.spark.streaming.dstream.{DStream, InputDStream}
import org.apache.spark.streaming.kafka.KafkaUtils
import org.apache.spark.streaming.{Seconds, StreamingContext}

/**
  * Created by dbort on 26.09.2016.
  */
object StreamMain {

  //spark-submit --class edu.dbortnichuk.spark.StreamMain --master local[*] --name StreamTest jars/spark-1.0-SNAPSHOT.jar  hdfs:///user/cloudera/files/json/

  def main(args: Array[String]): Unit = {

    val conf = new SparkConf()
    conf.setAppName("StreamingLogInput")
    conf.setMaster("local[*]")
    // Create a StreamingContext with a 1 second batch size
    val ssc = new StreamingContext(conf, Seconds(3))
    // Create a DStream from all the input on port 7777
    //    val lines = ssc.socketTextStream("192.168.159.226", 7777)
    //    val errorLines = processLines(lines)
    //    // Print out the lines with errors, which causes this DStream to be evaluated
    //    errorLines.print()
    // start our streaming context and wait for it to "finish"
    val kafkaParams = Map[String, String]("metadata.broker.list" -> "192.168.159.226:9092", "serializer.class" -> classOf[StringEncoder].getName, "auto.offset.reset" -> "largest")
    val kafkaStream: InputDStream[(String, String)] = KafkaUtils.createDirectStream[String, String, StringDecoder, StringDecoder](
      ssc,
      kafkaParams,
      Set("test"))

    kafkaStream.print()
    //kafkaStream.

    ssc.start()

    ssc.awaitTermination()
    ssc.stop()
  }

  def processLines(lines: DStream[String]) = {
    // Filter our DStream for lines with "error"
    lines.filter(_.contains("error"))
  }


}
