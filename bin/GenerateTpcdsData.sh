#!/bin/bash
# HDFS path of TPC-DS data

hadoop fs -mkdir -p /BenchmarkData/Tpcds
location=$(cd "$(dirname "$0")";pwd)

#spark-submit --class <class> <JAR_PATH>
# <DATA_SCALE> <ONLY_GENERATE_METASTORE> <DSAGEN_DIR> <HADOOP_HOST>

spark-submit --class org.BernardX.benchmark.Tpcds.GenerateTpcdsData ${location}/Benchmarks/jars/SparkBenchmarkSuite.jar \
  500 False ${location}/Benchmarks/tools/TPC-DS_v32/tools master0
