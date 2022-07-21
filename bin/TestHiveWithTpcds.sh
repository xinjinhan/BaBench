#!/bin/bash

# configurations
dataFormat=parquet
dataScale=500
selectedQueries=q1,q2,q3


# submit benchmark program
if hadoop fs -test -e /BenchmarkData/Tpcds/tpcds_${dataScale}/tpcds_${dataFormat}/web_site;then

location=$(cd "$(dirname "$0")";pwd)
querys=(`echo $selectedQueries | tr ',' ' '`)
durations=()
durationSum=0

function timediff() {
    # time format:date +"%s.%N"
    start_time=$1
    end_time=$2

    start_s=${start_time%.*}
    start_nanos=${start_time#*.}
    end_s=${end_time%.*}
    end_nanos=${end_time#*.}

    # end_nanos > start_nanos?
    # Another way, the time part may start with 0, which means
    # it will be regarded as oct format, use "10#" to ensure
    # calculateing with decimal
    if [ "$end_nanos" -lt "$start_nanos" ];then
        end_s=$(( 10#$end_s - 1 ))
        end_nanos=$(( 10#$end_nanos + 10**9 ))
    fi

    # get timediff
    time=$(( 10#$end_s - 10#$start_s )).`printf "%03d\n" $(( (10#$end_nanos - 10#$start_nanos)/10**6 ))`
    typeset time=$(echo "scale=3;$time * 1000" | bc)
    echo $time | awk -F. '{print $1}'
}

function runHive() {
  hive -i $location/../querySamples/tpcds/init.sql --database tpcds_$1_$2 -f $location/../querySamples/tpcds/$3
}
startTime=`date +'%Y-%m-%d_%H:%M:%S'`

for i in "${!querys[@]}";do
    startSecondsQuery=$(date +%s.%N)
    runHive $dataScale $dataFormat ${querys[$i]}.sql
    if [ "$?" -ne 0 ];then
      echo "Falied: Hive ${dataScale}GB Tpcds test failed at Query ${querys[$i]}."
      exit 1
    fi
    endSecondsQuery=$(date +%s.%N)
    durations[$i]=$(timediff $startSecondsQuery $endSecondsQuery)
    durationSum=$((durationSum+durations[$i]))
done

endTime=`date +'%Y-%m-%d_%H:%M:%S'`

else
   echo "${dataScale}GB Tpcds data does not exist in HDFS or has broken. Please re-generate it before testing."
   exit 1
fi

echo "Hive  Tpcds  (${querys[*]})  (${durations[*]})  $startTime  $endTime  $durationSum  $dataScale  $dataFormat  Succeed" >> $location/../reports/babench.report