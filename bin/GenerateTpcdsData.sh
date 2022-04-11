#!/bin/bash

# configurations
dataFormat=parquet
dataScale=500
skipDataGenAndCreateDatabase=False


# submit data generate program
hadoop fs -mkdir -p /BenchmarkData/Tpcds
location=$(cd "$(dirname "$0")";pwd)
#spark-submit --class <class> <JAR_PATH> <DATA_FORMAT> <DATA_SCALE> <SKIP_DATAGEN_CREATE_DATABASE> <DSAGEN_DIR> <HADOOP_HOST>
spark-submit --master yarn --num-executors 10 --executor-memory 2g --executor-cores 2 \
--class org.BernardX.spark.sql.perf.tpcds.GenerateTpcdsData ${location}/../jars/bigbench-core-1.0.jar \
$dataFormat \
$dataScale \
$skipDataGenAndCreateDatabase \
${location}/../tools/tpcds-kit/tools \
$HOSTNAME
