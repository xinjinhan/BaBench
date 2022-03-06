/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.BernardX.spark.sql.perf.tpch

import org.apache.spark.sql.SparkSession

object GenerateTpchData {
  def main(args: Array[String]): Unit = {
    if (args.length != 4) {
      System.err.println(
        s"Usage: $RunTpch <DATA_SCALE> <SKIP_DATAGENERATION> <DBGEN_DIR> <HADOOP_HOST>"
      )
      System.exit(1)
    }
    try {
      val scaleFactor: String = args(0)
      val skipDataGenerate: String = args(1)
      val dbgenDir = args(2)
      val hadoopHost = args(3)
      val rootDir = s"hdfs://$hadoopHost:9000/BenchmarkData/Tpch/tpch_$scaleFactor"
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
    } catch {
      case e: Exception =>
        e.printStackTrace()
    }
  }
}
