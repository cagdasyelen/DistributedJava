#!/bin/sh

# Set the varaibles here:
source settings/$1.cfg

DIRECTORY=./jpf-symbc/dist
if [ ! -d "$DIRECTORY" ]; then
	cd ./jpf-symbc-distributed
	./build.sh
	cd ../jpf-symbc
else
	cd ./jpf-symbc
fi

./run_s.sh -N -c $CLASS_NAME -m $METHOD_NAME -d $INITIAL_DEPTH -D $FINAL_DEPTH -C $CERT_FILE -a $METHOD_SHORT_NAME -r $2
