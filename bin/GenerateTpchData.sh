#!/bin/bash

# configurations
dataFormat=parquet
dataScale=500
skipDataGenAndCreateDatabase=False


# submit data generate program
hadoop fs -mkdir -p /BenchmarkData/Tpch
location=$(cd "$(dirname "$0")";pwd)
#spark-submit --class <class> <JAR_PATH> <DATA_SCALE> <SKIP_DATAGEN_CREATE_DATABASE> <DSAGEN_DIR> <HADOOP_HOST>
spark-submit --master yarn --num-executors 10 --executor-memory 2g --executor-cores 2 \
--class org.BernardX.spark.sql.perf.tpch.GenerateTpchData ${location}/../jars/babench-core-1.0.jar \
$dataFormat \
$dataScale \
$skipDataGenAndCreateDatabase \
${location}/../tools/tpch-kit/ \
$HOSTNAME