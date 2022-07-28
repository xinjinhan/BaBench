### Build through Maven ###
To simply build all modules in HiBench, use the below command. This could be time consuming because the hadoopbench relies on 3rd party tools like Mahout and Nutch. The build process automatically downloads these tools for you. If you won't run these workloads, you can only build a specific framework to speed up the build process.

    mvn clean package


### Specify Scala Version ###
To specify the Scala version, use -Dscala=xxx(2.11 or 2.12). By default, it builds for scala 2.12.

    mvn -Dscala=2.12 clean package


### Specify Spark Version ###
To specify the spark version, use -Dspark=xxx(2.2, 2.4, 3.0, 3.1, or 3.2). By default, it builds for spark 3.1

    mvn -Dspark=3.1 -Dscala=2.12 clean package
