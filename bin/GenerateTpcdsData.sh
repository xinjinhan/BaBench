#!/bin/bash

# configurations
dataScale=500
onlyInitializeMetastore=False


# submit data generate program
hadoop fs -mkdir -p /BenchmarkData/Tpcds
location=$(cd "$(dirname "$0")";pwd)
#spark-submit --class <class> <JAR_PATH>
spark-submit --class org.shuhai.spark.sql.perf.tpcds.GenerateTpcdsData ${location}/../jars/SparkBenchmarkSuite.jar \
  # <DATA_SCALE>
  $dataScale \
  # <ONLY_GENERATE_METASTORE>
  $onlyInitializeMetastore \
  # <DSAGEN_DIR>
  ${location}/../tools/tpcds-kit/tools \
  # <HADOOP_HOST>
  $HOSTNAME
