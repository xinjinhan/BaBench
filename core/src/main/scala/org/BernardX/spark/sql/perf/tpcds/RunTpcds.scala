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

package org.BernardX.spark.sql.perf.tpcds

import org.apache.spark.sql.SparkSession

import java.io.{File, FileWriter}
import java.text.SimpleDateFormat
import java.util.Date

object RunTpcds {
  def main(args: Array[String]): Unit = {
    if (args.length != 5) {
      System.err.println(
        s"Usage: $RunTpcds <DATA_FORMAT> <DATA_SCALE> <QUERY_LIST> <REPORT_LOCATION> <HADOOP_HOST>"
      )
      System.exit(1)
    }

    val dataFormat: String = args(0)
    val scaleFactor: String = args(1)
    val queryListString: String = args(2)
    val queryNames: Array[String] = queryListString.split(",")
    val reportLocation: String= args(3)
    val hadoopHost: String = args(4)

    val reportDurationFile = new FileWriter(s"${File.separator}$reportLocation${File.separator}babench.report",true)
    val dateFrame: SimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss")

    try {
      val sqlContext = SparkSession
        .builder()
        .appName(s"TPCDS_${scaleFactor}GB_${queryListString}_$dataFormat")
        .enableHiveSupport()
        .getOrCreate()
        .sqlContext

      val tpcds = new TPCDS(sqlContext)
      val databaseName = s"tpcds_${scaleFactor}_$dataFormat"

      val resultLocation = s"hdfs://$hadoopHost:9000/BenchmarkData/Tpcds/Results"
      val iteration = 1
      val queryMap = tpcds.tpcds2_4QueriesMap
      val timeout = 100000
      val queries = queryNames.map(queryName => queryMap(queryName))
      val appId = sqlContext.sparkContext.applicationId

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
      reportDurationFile.write(s"Spark,TPC-DS,($queryListString),${times.mkString("(",",",")")},$startTime," +
        s",$stopTime,$duration,${scaleFactor}GB,$dataFormat,$appId,Succeed\n")
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
