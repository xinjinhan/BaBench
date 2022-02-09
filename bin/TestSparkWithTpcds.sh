#!/bin/bash
location=$(cd "$(dirname "$0")";pwd)

#spark-submit --class <class> <JAR_PATH>
# <DATA_SCALE> <QUERY_LIST> <REPORT_LOCATION> <HADOOP_HOST>

spark-submit --class org.BernardX.benchmark.Tpcds.RunTpcds ${location}/Benchmarks/jars/SparkBenchmarkSuite.jar \
  500 q1,q2,q3 ${location}/Reports master0
