# Bigdata Benchmark suite (BigBench)
## A scalable, easy to use, and user-friendly benchmark suite.
* Homepage: https://github.com/xinjinhan/BigBench.git
* Contents:
    1. Overview
    2. Getting Started
    3. To Do List
    
---
## OVERVIEW ##

BigBench is a big data benchmark suite that helps evaluate different big data framework ( such as [Spark SQL](https://github.com/apache/spark), [Hive](https://github.com/apache/hive), [Impala](https://github.com/apache/impala), and etc ). By now, BigBench contains TPC-DS and TPC-H, two commonly used decision support system benchmarks. BigBench can be easily used to benchmark Spark. BigBench will support more benchmarks in the future. BigBench will also support cloud-native systems and service monitoring system ( such as [prometheus](https://github.com/prometheus/prometheus) ) later.  

---
## Test with Spark SQL ##
Before your test, make sure you have deployed hadoop and spark environment, checking with commands:
```
hadoop version
```
```
spark-shell --version
（***spark2x with scala2.11.x please pull branch spark2x_scala211***）
```
### 1.specify slaves of your cluster
* Copy "[slaves.template](conf/slaves.template)" to "slaves" in folder [conf](conf).
* Specify the hostname/ip of every node, one hostname/ip per line. Such as:

```
slave1
slave2
slabe3
```

### 2.Initialize the environment
* Execute [bin/InitializeEnvironment.sh](bin/InitializeEnvironment.sh)
### 3. Start TPC-DS Benchmark
### 1) Genenrate TPC-DS Data
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
### 2) Run TPC-DS Benchmark
* Specify the configuration in [bin/TestSparkWithTpcds.sh](bin/TestSparkWithTpcds.sh):
  
  ***datascale*** ( decides the data scale of TPC-DS benchmark, in GB )
  
  ***selectedQueries*** ( decides which queries of TPC-DS to be tested )
```
#!/bin/bash

# configurations
dataScale=500
selectedQueries=q1,q2,q3

```

### 4. Start TPC-H Benchmark
### 1) Genenrate TPC-H Data
* Specify the configuration in [bin/GenerateTpchData.sh](bin/GenerateTpcdsData.sh):

  ***datascale*** ( decides the data scale of generated data, in GB )

  ***onlyInitializeMetastore*** ( ***usually keep it False***, decides whether to skip the data generating and create tables directly )
```
#!/bin/bash

# configurations
dataScale=500
onlyInitializeMetastore=False
```

* Execute the [bin/GenerateTpchData.sh](bin/GenerateTpcdsData.sh) in the master node.
### 2) Run TPC-H Benchmark
* Specify the configuration in [bin/TestSparkWithTpch.sh](bin/TestSparkWithTpch.sh):
  
  ***datascale*** ( decides the data scale of TPC-DS benchmark， in GB )

  ***selectedQueries*** ( decides which queries of TPC-DS to be tested )
```
#!/bin/bash

# configurations
dataScale=500
selectedQueries=q1,q2,q3

```

### 5. Results
* BigBench saves detailed results into HDFS (/BenchmarkData/Tpcds/Results)
* BigBench saves simplified results into [bigbench.report](/reports/bigbench.report), including:
```
BenchmarkName     Queries     Durations    StartAt     StopAt    DurationSum      Datasize     FinalStatus
```
---
## TO DO List ##
1. Support deployment and visualization of cluster monitering;
2. Integrate with more benchmark (such as hibench);
3. Support more bigdata framework;
