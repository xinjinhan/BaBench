#!/bin/bash
# HDFS path of TPC-DS data
hadoop fs -mkdir -p /BenchmarkData/Tpcds
location=$(cd "$(dirname "$0")";pwd)

#spark-submit --class <class> <JAR_PATH>
spark-submit --class org.shuhai.spark.sql.perf.tpcds.GenerateTpcdsData ${location}/../jars/SparkBenchmarkSuite.jar \
  # <DATA_SCALE>
  500 \
  # <ONLY_GENERATE_METASTORE>
  False \
  # <DSAGEN_DIR>
  ${location}/../tools/tpcds-kit/tools \
  # <HADOOP_HOST>
  $HOSTNAME
