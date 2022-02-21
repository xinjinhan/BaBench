#!/bin/bash
location=$(cd "$(dirname "$0")";pwd)

#spark-submit --class <class> <JAR_PATH>
spark-submit --class org.BernardX.benchmark.Tpcds.RunTpcds ${location}/Benchmarks/jars/SparkBenchmarkSuite.jar \
  # <DATA_SCALE>
  500 \
  # <QUERY_LIST>
  q1,q2,q3 \
  # <REPORT_LOCATION>
  ${location}/Reports
  # <HADOOP_HOST>
  $HOSTNAME
