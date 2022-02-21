#!/bin/bash

# configurations
dataScale=500
onlyInitializeMetastore=False


# submit data generate program
hadoop fs -mkdir -p /BenchmarkData/Tpch
location=$(cd "$(dirname "$0")";pwd)
#spark-submit --class <class> <JAR_PATH>
spark-submit --class org.shuhai.spark.sql.perf.tpch.GenerateTpchData ${location}/../jars/SparkBenchmarkSuite.jar \
  # <DATA_SCALE>
  $dataScale \
  # <ONLY_GENERATE_METASTORE>
  $onlyInitializeMetastore \
  # <DSAGEN_DIR>
  ${location}/../tools/tpch-kit/dbgen \
  # <HADOOP_HOST>
  $HOSTNAME