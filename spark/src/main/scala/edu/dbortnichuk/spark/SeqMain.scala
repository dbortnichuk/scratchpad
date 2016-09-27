package edu.dbortnichuk.spark

import java.net.URI

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.{FileSystem, Path}
import org.apache.hadoop.io._
import org.apache.hadoop.mapred.{SequenceFileOutputFormat, TextInputFormat}
import org.apache.hadoop.mapreduce.{Job, Mapper, Reducer}
import org.apache.spark.{SparkConf, SparkContext}

import scala.io.Source

/**
  * Created by dbort on 20.09.2016.
  */
object SeqMain {

  def main(args: Array[String]): Unit = {

    //val samplePath: String = Main.getPathToRes("sample_genome_fasta.txt")
    val sampleLines: Iterator[String] = Source.fromFile(args(0)).getLines()

    val conf: Configuration = new Configuration()
    val fs: FileSystem = FileSystem.get(URI.create(args(1)), conf)
    val path: Path = new Path(args(1))
    val key: IntWritable = new IntWritable()
    val value: Text = new Text()
    var writer: SequenceFile.Writer = null
    try {
      writer = SequenceFile.createWriter( fs, conf, path, key.getClass(), value.getClass())
      var keyIdx: Int = 0
      while(sampleLines.hasNext){
        key.set(keyIdx)
        keyIdx += 1
        value.set(sampleLines.next())
        //System.out.printf("[% s]\t% s\t% s\n", writer.getLength(), key, value)
        writer.append(key, value)
      }
    } finally{
      IOUtils.closeStream( writer)
    }




  }

}
