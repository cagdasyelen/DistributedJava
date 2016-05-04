#!/bin/sh
# Run the project

# change the java command here
RUNJAVA=/usr/bin/java
# change the main class here
MAIN_CLASS=edu.utexas.exec.ParallelSPF

CURRENT_PATH=`pwd`
echo "$@"
echo "jpf-symbc path:" $CURRENT_PATH
export LD_LIBRARY_PATH=$CURRENT_PATH/lib
echo "Environment Variable DYLD_LIBRARY_PATH:" $DYLD_LIBRARY_PATH
cd ./dist
$RUNJAVA -Xms512m -Xmx5g -cp ./:../../jpf-core/build/*:../build/*:../lib/*:../../jpf-symbc-distributed/lib/* $MAIN_CLASS $@
