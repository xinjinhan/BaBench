package org.BernardX.benchmark.Tpch

import com.databricks.spark.sql.perf.tpch.TPCHTables
import org.apache.spark.sql.SparkSession

object GenerateTpchData {
  def main(args: Array[String]): Unit = {
    val scaleFactor: String = args(0)
    val skipDataGenerate: String = args(1)
    val rootDir = "hdfs://master0:9000/BenchmarkData/Tpch/tpch_" + scaleFactor
    val dbgenDir = "/home/BernardX/SQLPerf/tpch/dbgen"
    val format = "parquet"
    val databaseName = "tpch_" + scaleFactor +"_parquet"

    val sparkSession = SparkSession
      .builder()
      .enableHiveSupport()
      .getOrCreate()

    val sqlContext = sparkSession.sqlContext

    val tables = new TPCHTables(sparkSession.sqlContext,
      dbgenDir = dbgenDir,
      scaleFactor = scaleFactor,
      useDoubleForDecimal = true,
      useStringForDate = true)

    if (!skipDataGenerate.toBoolean) {
      tables.genData(
        location = rootDir,
        format = format,
        overwrite = true,
        partitionTables = false,
        clusterByPartitionColumns = false,
        filterOutNullPartitionValues = false,
        numPartitions = 120)
    }
    //创建临时表
    tables.createTemporaryTables(rootDir, format)
    //将表信息注册到 hive metastore
    sqlContext.sql(s"create database $databaseName")
    tables.createExternalTables(rootDir, format, databaseName, overwrite = true, discoverPartitions = false)
  }
}
