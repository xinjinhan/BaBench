#!/bin/bash

# configurations
dataFormat=parquet
dataScale=500
selectedQueries=q1,q2,q3


# submit benchmark program
if hadoop fs -test -e /BenchmarkData/Tpch/tpch_${dataScale}/tpcds_${dataFormat}/region;then
location=$(cd "$(dirname "$0")";pwd)
# spark-submit --class <class> <JAR_PATH> <DATA_SCALE> <QUERY_LIST> <REPORT_LOCATION> <HADOOP_HOST>
spark-submit --master yarn --class org.BernardX.spark.sql.perf.tpch.RunTpch ${location}/../jars/babench-core-1.0.jar \
$dataFormat \
$dataScale \
$selectedQueries \
${location}/../reports \
$HOSTNAME

else
   echo "${dataScale}GB ${dataFormat} format Tpch data does not exist in HDFS or has broken. Please re-generate it before testing."
   exit 1
fi