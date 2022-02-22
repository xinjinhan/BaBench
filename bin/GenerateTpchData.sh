#!/bin/bash

# configurations
dataScale=500
onlyInitializeMetastore=False


# submit data generate program
hadoop fs -mkdir -p /BenchmarkData/Tpch
location=$(cd "$(dirname "$0")";pwd)
#spark-submit --class <class> <JAR_PATH> <DATA_SCALE> <ONLY_GENERATE_METASTORE> <DSAGEN_DIR> <HADOOP_HOST>
spark-submit --class org.shuhai.spark.sql.perf.tpch.GenerateTpchData ${location}/../jars/BenchmarkSuites.jar \
$dataScale \
$onlyInitializeMetastore \
${location}/../tools/tpch-kit/dbgen \
$HOSTNAME