#!/bin/bash

# configurations
dataScale=500
selectedQueries=q1,q2,q3


# submit benchmark program
if hadoop fs -test -e /BenchmarkData/Tpcds/tpcds_${dataScale}/web_site;then

location=$(cd "$(dirname "$0")";pwd)
querys=(`echo $selectedQueries | tr ',' ' '`)
durations=()
durationSum=0

function runHive() {
  hive -i $location/../querySamples/tpcds/init.sql --database tpcds_$1_parquet -f $location/../querySamples/tpcds/$2
}
startTime=`date +'%Y-%m-%d_%H:%M:%S'`

for i in "${!querys[@]}";do
    startTimeQuery=`date +'%Y-%m-%d_%H:%M:%S'`
    startSecondsQuery=$(date --date="$startTimeQuery" +%s)
    runHive $dataScale ${querys[$i]}.sql
    if [ "$?" -ne 0 ];then
      echo "Falied: Hive ${dataScale}GB Tpcds test failed at Query ${querys[$i]}."
      exit 1
    fi
    endTimeQuery=`date +'%Y-%m-%d_%H:%M:%S'`
    endSecondsQuery=$(date --date="$endTimeQuery" +%s)
    durations[$i]=$((startSecondsQuery-endSecondsQuery))
    durationSum=$((durationSum+durations[$i]))
done

endTime=`date +'%Y-%m-%d_%H:%M:%S'`

else
   echo "${dataScale}GB Tpcds data does not exist in HDFS or has broken. Please re-generate it before testing."
   exit 1
fi

echo "Hive  Tpcds  (${querys[*]})  (${durations[*]})  $startTime  $endTime  $durationSum  $dataScale  Succeed" >> $location/../reports/bigbench.report