package org.shuhai.spark.sql.perf.tpcds

import org.apache.spark.sql.SparkSession

object RunTpcds {
  def main(args: Array[String]): Unit = {
    if (args.length != 4) {
      System.err.println(
        s"Usage: $RunTpcds <DATA_SCALE> <QUERY_LIST> <HADOOP_HOST>"
      )
      System.exit(1)
    }
    try {
      val scaleFactor: String = args(0)
      val queryListString: String = args(1)
      val queryNames: Array[String] = queryListString.split(",")
      val hadoopHost: String = args(3)

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
      sqlContext.sql(s"use $databaseName")

      val experiment = tpcds.runExperiment(
        queries,
        iterations = iteration,
        resultLocation = resultLocation)

      experiment.waitForFinish(timeout)
    }
    catch {
      case e: Exception =>
        e.printStackTrace()
    }
  }
}
