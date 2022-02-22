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
cd $location/../tools/tpcds-kit/tools/ && make
cd $location/../tools/tpch-kit/dbgen && make

for node in $(cat $location/../conf/slaves)
# copy benchmark tools to every node
do
  ssh $node "mkdir -p $location/.."
  scp -r $location/../tools $node:$location/..
done
