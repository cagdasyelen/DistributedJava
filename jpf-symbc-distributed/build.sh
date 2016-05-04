#!/bin/sh
# Build the project

# change the javac command here
RUNJAVAC=/usr/bin/javac

BUILD_DIRECTORY=../jpf-symbc/dist
if [ -d $BUILD_DIRECTORY ]; then
	echo "Deleting existing build directory..."
	./clean.sh
fi
echo "Start building the project..."
mkdir $BUILD_DIRECTORY
SOURCES=./sources.txt
if [ -f $SOURCES ] ; then
    rm $SOURCES
fi
find . -name "*.java" > $SOURCES
$RUNJAVAC -g -cp ../jpf-core/build/*:../jpf-symbc/build/*:../jpf-symbc/lib/*:./lib/* -d $BUILD_DIRECTORY @sources.txt
echo "Done"
