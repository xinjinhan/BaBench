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

package org.BernardX.spark.sql.perf

class AggregationPerformance extends Benchmark {

  import sqlContext.implicits._
  import ExecutionMode._


  val sizes = (1 to 6).map(math.pow(10, _).toInt)

  val x = Table(
    "1milints", {
      val df = sqlContext.range(0, 1000000).repartition(1)
      df.createTempView("1milints")
      df
    })

  val joinTables = Seq(
    Table(
      "100milints", {
        val df = sqlContext.range(0, 100000000).repartition(10)
        df.createTempView("100milints")
        df
      }),

    Table(
      "1bilints", {
        val df = sqlContext.range(0, 1000000000).repartition(10)
        df.createTempView("1bilints")
        df
      }
    )
  )

  val variousCardinality = sizes.map { size =>
    Table(s"ints$size", {
      val df = sparkContext.parallelize(1 to size).flatMap { group =>
        (1 to 10000).map(i => (group, i))
      }.toDF("a", "b")
      df.createTempView(s"ints$size")
      df
    })
  }

  val lowCardinality = sizes.map { size =>
    val fullSize = size * 10000L
    Table(
      s"twoGroups$fullSize", {
        val df = sqlContext.range(0, fullSize).select($"id" % 2 as 'a, $"id" as 'b)
        df.createTempView(s"twoGroups$fullSize")
        df
      })
  }

  val newAggreation = Variation("aggregationType", Seq("new", "old")) {
    case "old" => sqlContext.setConf("spark.sql.useAggregate2", "false")
    case "new" => sqlContext.setConf("spark.sql.useAggregate2", "true")
  }

  val varyNumGroupsAvg: Seq[Benchmarkable] = variousCardinality.map(_.name).map { table =>
    Query(
      s"avg-$table",
      s"SELECT AVG(b) FROM $table GROUP BY a",
      "an average with a varying number of groups",
      executionMode = ForeachResults)
  }

  val twoGroupsAvg: Seq[Benchmarkable] = lowCardinality.map(_.name).map { table =>
    Query(
      s"avg-$table",
      s"SELECT AVG(b) FROM $table GROUP BY a",
      "an average on an int column with only two groups",
      executionMode = ForeachResults)
  }

  val complexInput: Seq[Benchmarkable] =
    Seq("1milints", "100milints", "1bilints").map { table =>
      Query(
        s"aggregation-complex-input-$table",
        s"SELECT SUM(id + id + id + id + id + id + id + id + id + id) FROM $table",
        "Sum of 9 columns added together",
        executionMode = CollectResults)
    }

  val aggregates: Seq[Benchmarkable] =
    Seq("1milints", "100milints", "1bilints").flatMap { table =>
      Seq("SUM", "AVG", "COUNT", "STDDEV").map { agg =>
        Query(
          s"single-aggregate-$agg-$table",
          s"SELECT $agg(id) FROM $table",
          "aggregation of a single column",
          executionMode = CollectResults)
      }
    }
}