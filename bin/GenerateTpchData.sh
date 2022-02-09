#!/bin/bash
location=$(cd "$(dirname "$0")";pwd)
spark-submit --class org.BernardX.benchmark.Tpch.GenerateTpchData ${location}/../out/artifacts/SparkBenchmarkSuite_jar/SparkBenchmarkSuite.jar