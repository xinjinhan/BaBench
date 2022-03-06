#!/bin/bash

# configurations
dataScale=500
onlyInitializeMetastore=False


# submit data generate program
hadoop fs -mkdir -p /BenchmarkData/Tpcds
location=$(cd "$(dirname "$0")";pwd)
#spark-submit --class <class> <JAR_PATH> <DATA_SCALE> <ONLY_GENERATE_METASTORE> <DSAGEN_DIR> <HADOOP_HOST>
spark-submit --master yarn --num-executors 10 --executor-memory 2g --executor-cores 2 \
--class org.BernardX.spark.sql.perf.tpcds.GenerateTpcdsData ${location}/../jars/bigbench-core-1.0.jar \
$dataScale \
$onlyInitializeMetastore \
${location}/../tools/tpcds-kit/tools \
$HOSTNAME
