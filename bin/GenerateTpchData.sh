#!/bin/bash
# HDFS path of TPC-H data
hadoop fs -mkdir -p /BenchmarkData/Tpch
location=$(cd "$(dirname "$0")";pwd)

#spark-submit --class <class> <JAR_PATH>
spark-submit --class org.shuhai.spark.sql.perf.tpch.GenerateTpchData ${location}/../jars/SparkBenchmarkSuite.jar \
  # <DATA_SCALE>
  500 \
  # <ONLY_GENERATE_METASTORE>
  False \
  # <DSAGEN_DIR>
  ${location}/../tools/tpch-kit/dbgen \
  # <HADOOP_HOST>
  $HOSTNAME