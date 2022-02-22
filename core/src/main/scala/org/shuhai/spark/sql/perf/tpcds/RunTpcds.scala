package org.shuhai.spark.sql.perf.tpcds

import org.apache.spark.sql.SparkSession

import java.io.{File, FileWriter}
import java.text.SimpleDateFormat
import java.util.Date

object RunTpcds {
  def main(args: Array[String]): Unit = {
    if (args.length != 4) {
      System.err.println(
        s"Usage: $RunTpcds <DATA_SCALE> <QUERY_LIST> <REPORT_LOCATION> <HADOOP_HOST>"
      )
      System.exit(1)
    }

      val scaleFactor: String = args(0)
      val queryListString: String = args(1)
      val queryNames: Array[String] = queryListString.split(",")
      val reportLocation: String= args(2)
      val hadoopHost: String = args(3)
      val reportDurationFile = new FileWriter(s"${File.separator}$reportLocation${File.separator}bigbench.report",true)
      val dateFrame: SimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss")

    try {
      val sqlContext = SparkSession
        .builder()
        .enableHiveSupport()
        .getOrCreate()
        .sqlContext

      val tpcds = new TPCDS(sqlContext)
      val databaseName = s"tpcds_${scaleFactor}_parquet"

      val resultLocation = s"hdfs://$hadoopHost:9000/BenchmarkData/Tpcds/Results"
      val iteration = 1
      val queryMap = tpcds.tpcds2_4QueriesMap
      val timeout = 100000
      val queries = queryNames.map(queryName => queryMap(queryName))

      val startTime = dateFrame.format(new Date())
      sqlContext.sql(s"use $databaseName")
      val experiment = tpcds.runExperiment(
        queries,
        iterations = iteration,
        resultLocation = resultLocation)
      experiment.waitForFinish(timeout)
      val stopTime = dateFrame.format(new Date())

      val results = experiment.getFinalResults()
      val times = results.map(res => res.executionTime.get.toInt).toList
      val duration = times.sum
      reportDurationFile.write(s"TPC-DS  ($queryListString)  $times  $startTime" +
        s"  $stopTime  $duration  ${scaleFactor}GB  Succeed\n")
    }
    catch {
      case e: Exception =>
        e.printStackTrace()
    }
    finally {
      reportDurationFile.close()
    }
  }
}
