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
    if (args.length != 5) {
      System.err.println(
        s"Usage: $RunTpch <DATA_FORMAT> <DATA_SCALE> <SKIP_DATAGEN_CREATE_DATABASE> <DSAGEN_DIR> <HADOOP_HOST>"
      )
      System.exit(1)
    }
    try {
      val dataFormat = args(0)
      val scaleFactor: String = args(1)
      val skipDataGenerate: String = args(2)
      val dbgenDir = args(3)
      val hadoopHost = args(4)

      val rootDir = s"hdfs://$hadoopHost:9000/BenchmarkData/Tpch/tpch_$scaleFactor/tpch_$dataFormat"
      val databaseName = s"tpch_${scaleFactor}_$dataFormat"

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
          format = dataFormat,
          overwrite = true,
          partitionTables = false,
          clusterByPartitionColumns = false,
          filterOutNullPartitionValues = false,
          numPartitions = 120)
      }

      //Create tmp table
      tables.createTemporaryTables(rootDir, dataFormat)
      //Register table to hive metastore
      sqlContext.sql(s"create database $databaseName")
      tables.createExternalTables(rootDir, dataFormat, databaseName, overwrite = true, discoverPartitions = false)
    } catch {
      case e: Exception =>
        e.printStackTrace()
    }
  }
}
