#!/bin/bash

# configurations
dataScale=500
selectedQueries=q1,q2,q3


# submit benchmark program
if hadoop fs -test -e /BenchmarkData/Tpch/tpch_$datascale_parquet/region;then
  location=$(cd "$(dirname "$0")";pwd)
  # spark-submit --class <class> <JAR_PATH>
  spark-submit --class org.shuhai.spark.sql.perf.tpch.RunTpch ${location}/Benchmarks/jars/SparkBenchmarkSuite.jar \
    # <DATA_SCALE>
    $dataScale \
    # <QUERY_LIST>
    $selectedQueries \
    # <REPORT_LOCATION>
    ${location}/Reports \
    # <HADOOP_HOST>
    $HOSTNAME
else
   echo "$dataScale GB Tpcds data does not exist. Please generate it before test."
   exit 1
fi