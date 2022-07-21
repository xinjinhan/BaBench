# Bigdata Benchmark suite (BaBench)
## A scalable, easy to use OLAP benchmark suite.
* Homepage: https://github.com/xinjinhan/BaBench.git
* Contents:
    1. Overview
    2. Getting Started
       1) Build Babench
       2) Configuration
       3) Initialize the Environment
       4) Prepare Benchmark Data
       5) Run benchmark
---
## OVERVIEW ##

babench is a big data benchmark suite that helps evaluate different big data framework ( such as [Spark SQL](https://github.com/apache/spark), [Hive](https://github.com/apache/hive), [Impala](https://github.com/apache/impala), and etc ). By now, babench contains TPC-DS and TPC-H, two commonly used decision support system benchmarks. babench can be easily used to benchmark Spark, Hive and etc. babench will support more benchmarks in the future. babench will also support cloud-native systems and service monitoring system ( such as [prometheus](https://github.com/prometheus/prometheus) ) later.  


---
## Getting Started ##
Before your test, make sure you have deployed hadoop and spark environment, checking with commands:
```
hadoop version
```
```
spark-shell --version
```
---
### 1. Build babench ###
* [Build babench](docs/babench-build.md)


### 2. Configure `slaves` ###
* Copy "[slaves.template](conf/slaves.template)" to "slaves" in folder [conf](conf).
* Specify the hostname/ip of every node, one hostname/ip per line. Such as:

```
slave1
slave2
slave3
```

### 3.Initialize the Environment ###
* Execute [bin/InitializeEnvironment.sh](bin/InitializeEnvironment.sh)

### 4. Prepare benchmark Data ###
babench generates benchmark data based on Spark. Making sure you have Spark environment in your cluster. And the more resources allocated to Spark, the faster data is generated. For details about Spark Tuning, see [Spark Tuning Guides](http://spark.incubator.apache.org/docs/latest/tuning.html).
### 1) Genenrate TPC-DS Data ###
* Specify the configuration in [bin/GenerateTpcdsData.sh](bin/GenerateTpcdsData.sh):
  
  ***datascale*** ( decides the data scale of generated data, in GB )
  
  ***onlyInitializeMetastore*** ( ***usually keep it False***, decides whether to skip the data generating and create tables directly )
```
#!/bin/bash

# configurations
dataScale=500
onlyInitializeMetastore=False
```
* Execute the [bin/GenerateTpcdsData.sh](bin/GenerateTpcdsData.sh) in the master node.


### 2) Genenrate TPC-H Data ###
* Specify the configuration in [bin/GenerateTpchData.sh](bin/GenerateTpcdsData.sh):

  ***datascale*** ( decides the data scale of generated data, in GB )

  ***onlyInitializeMetastore*** ( ***usually keep it False***, decides whether to skip the data generating and create tables directly )
```
#!/bin/bash

# configurations
dataScale=500
onlyInitializeMetastore=False
```

* Execute the [bin/GenerateTpchData.sh](bin/GenerateTpchData.sh) in the master node.

### 5. Start Benchmarking ###
Currently, babench provides test scripts of Spark and Hive.
### 1) Run TPC-DS Benchmark ###
* Specify the configuration in [bin/TestSparkWithTpcds.sh](bin/TestSparkWithTpcds.sh) or [bin/TestHiveWithTpcds.sh](bin/TestHiveWithTpcds.sh):
  
  ***datascale*** ( decides the data scale of TPC-DS benchmark, in GB )
  
  ***selectedQueries*** ( decides which queries of TPC-DS to be tested, all queries can be found in [querySamples/tpcds](querySamples/tpcds) )
```
#!/bin/bash

# configurations
dataScale=500
selectedQueries=q1,q2,q3

```
* Directly run [bin/TestSparkWithTpcds.sh](bin/TestSparkWithTpcds.sh) or [bin/TestHiveWithTpcds.sh](bin/TestHiveWithTpcds.sh):


### 2) Run TPC-H Benchmark ###
* Specify the configuration in [bin/TestSparkWithTpch.sh](bin/TestSparkWithTpch.sh) or [bin/TestHiveWithTpch.sh](bin/TestHiveWithTpch.sh):
  
  ***datascale*** ( decides the data scale of TPC-DS benchmark, in GB )

  ***selectedQueries*** ( decides which queries of TPC-DS to be tested, all queries can be found in [querySamples/tpch](querySamples/tpch) )
```
#!/bin/bash

# configurations
dataScale=500
selectedQueries=q1,q2,q3

```

* Directly run [bin/TestSparkWithTpch.sh](bin/TestSparkWithTpch.sh) or [bin/TestHiveWithTpch.sh](bin/TestHiveWithTpch.sh):


### 5. Benchmark Results ###
* babench saves main results into [babench.report](/reports/babench.report), shown as:
```
Framework    BenchmarkName     Queries     Durations    StartAt     StopAt    DurationSum      Datasize     FinalStatus
```
---
