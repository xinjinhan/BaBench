#!/bin/bash

# configurations
dataScale=500
selectedQueries=q1,q2,q3


# submit benchmark program
if hadoop fs -test -e /BenchmarkData/Tpcds/tpcds_${datascale}_parquet/web_site;then
  location=$(cd "$(dirname "$0")";pwd)
  # spark-submit --class <class> <JAR_PATH>
  spark-submit --class org.shuhai.benchmark.Tpcds.RunTpcds ${location}/../jars/BenchmarkSuites.jar \
    # <DATA_SCALE>
    $dataScale \
    # <QUERY_LIST>
    $selectedQueries \
    # <REPORT_LOCATION>
    ${location}/Reports
    # <HADOOP_HOST>
    $HOSTNAME
else
   echo "$dataScale GB Tpcds data does not exist in HDFS of has broken. Please re-generate it before testing."
   exit 1
fi


