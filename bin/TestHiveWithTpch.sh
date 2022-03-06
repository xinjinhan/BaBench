#!/bin/bash

# configurations
dataScale=500
selectedQueries=q1,q2,q3


# submit benchmark program
if hadoop fs -test -e /BenchmarkData/Tpch/tpch_${dataScale}/region;then

location=$(cd "$(dirname "$0")";pwd)
querys=(`echo $selectedQueries | tr ',' ' '`)
durations=()
durationSum=0

function runHive() {
  hive -i $location/../querySamples/tpch/init.sql --database tpch_$1_parquet -f $location/../querySamples/tpch/$2
}
startTime=`date +'%Y-%m-%d_%H:%M:%S'`

for i in "${!querys[@]}";do
    startSecondsQuery=$(date +%s)
    runHive $dataScale ${querys[$i]}.sql
    if [ "$?" -ne 0 ];then
      echo "Falied: Hive ${dataScale}GB Tpch test failed at Query ${querys[$i]}."
      exit 1
    fi
    endSecondsQuery=$(date +%s)
    durations[$i]=$((startSecondsQuery-endSecondsQuery))
    durationSum=$((durationSum+durations[$i]))
done

endTime=`date +'%Y-%m-%d_%H:%M:%S'`

else
   echo "${dataScale}GB Tpch data does not exist in HDFS or has broken. Please re-generate it before testing."
   exit 1
fi

echo "Hive  Tpch  ($querys)  ($durations)  $startTime  $endTime  $durationSum  $dataScale  Succeed" >> $location/../reports/bigbench.report