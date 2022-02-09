#!/bin/bash
hadoop fs -mkdir -p /BenchmarkData/Tpcds
hadoop fs -mkdir /spark3HistoryLog
hadoop fs -put $SPARK_HOME/jars /
hadoop fs -mv /jars /spark3Jars