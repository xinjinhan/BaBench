#!/bin/bash
location=$(cd "$(dirname "$0")";pwd)
spark-submit --class org.BernardX.benchmark.Tpch.RunTpch ${location}/../jars/SparkBenchmarkSuite.jar 50 Q1 ${location}/../../Reports
