#!/bin/sh

# Set the varaibles here:
source ./settings/$@.cfg

DIRECTORY=./jpf-symbc/dist
if [ ! -d "$DIRECTORY" ]; then
	cd ./jpf-symbc-distributed
	./build.sh
	cd ../jpf-symbc
else
	cd ./jpf-symbc
fi

./run_m.sh -T -c $CLASS_NAME -m $METHOD_NAME -D $FINAL_DEPTH -a $METHOD_SHORT_NAME
