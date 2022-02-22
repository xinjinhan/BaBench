#!/bin/bash

# configurations
dataScale=500
selectedQueries=q1,q2,q3


# submit benchmark program
if hadoop fs -test -e /BenchmarkData/Tpch/tpch_${datascale}_parquet/region;then
location=$(cd "$(dirname "$0")";pwd)
# spark-submit --class <class> <JAR_PATH>
spark-submit --class org.shuhai.spark.sql.perf.tpch.RunTpch ${location}/../jars/BenchmarkSuites.jar \
# <DATA_SCALE>
$dataScale \
# <QUERY_LIST>
$selectedQueries \
# <REPORT_LOCATION>
${location}/Reports \
# <HADOOP_HOST>
$HOSTNAME

else
   echo "${dataScale}GB Tpch data does not exist in HDFS of has broken. Please re-generate it before testing."
   exit 1
fi