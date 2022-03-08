#!/bin/bash

# start hadoop
start-dfs.sh
start-yarn.sh

location=$(cd "$(dirname "$0")";pwd)

if [ ! -f "$location/../conf/slaves" ];then
  echo "Initializing failed. Please specify slaves of your cluster in $location/../conf/slaves"
  exit 1
fi

# make benchmark tools
function makeBenchmarks() {
 tar -zxvf $location/../tools/tpcds-kit.tar.gz -C $location/../tools/
 tar -zxvf $location/../tools/tpch-kit.tar.gz -C $location/../tools/
 cd $location/../tools/tpcds-kit/tools/ && make
 cd $location/../tools/tpch-kit/dbgen && make
}

makeBenchmarks
if [ "$?" -ne 0 ];then
   echo "Falied: cannot make benchmark."
   exit 1
fi

for node in $(cat $location/../conf/slaves)
# copy benchmark tools to every node
do
  ssh $node "mkdir -p $location/.."
  scp -r $location/../tools $node:$location/..
done
