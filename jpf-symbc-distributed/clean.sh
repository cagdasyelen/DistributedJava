#!/bin/sh
# Clean the build of the project
echo "Clean the build directory..."
DIRECTORY=../jpf-symbc/dist
echo Build dir is $DIRECTORY
rm -rf $DIRECTORY
echo "Done"