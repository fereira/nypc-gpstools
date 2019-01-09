#!/bin/bash

BUILD_DIR=/cul/src/javadev
export CLASSPATH=$BUILD_DIR/bin/:$BUILD_DIR/lib/*
# echo $CLASSPATH
java -classpath $CLASSPATH net.fereira.gps.GPX2Text $*
