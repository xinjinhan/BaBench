package org.shuhai.spark.sql.perf.tpch

import org.apache.spark.sql.SparkSession

object RunTpch {
  def main(args: Array[String]): Unit = {
    if (args.length != 2) {
      System.err.println(
        s"Usage: $RunTpch <DATA_SCALE> <QUERY_LIST> <HADOOP_HOST>"
      )
      System.exit(1)
    }
    val scaleFactor: String = args(0)
    val queryListString: String = args(1)
    val queryNames: Array[String] = queryListString.split(",")
    val hadoopHost: String = args(3)

    val sqlContext = SparkSession
      .builder()
      .enableHiveSupport()
      .getOrCreate()
      .sqlContext

    val tpch = new TPCH(sqlContext)
    val databaseName = s"tpcds_${scaleFactor}_parquet"

    val resultLocation = s"hdfs://$hadoopHost:9000/BenchmarkData/Tpcds/Results"
    val iteration = 1
    val queryMap = tpch.queriesMap
    val timeout = 300000
    val queries = queryNames.map(queryName => queryMap(queryName.replace("q","Q")))

    sqlContext.sql(s"use $databaseName")

    val experiment = tpch.runExperiment(
      queries,
      iterations = iteration,
      resultLocation = resultLocation)

    experiment.waitForFinish(timeout)
  }
}
