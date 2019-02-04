#!/bin/bash

export CLASSPATH=../target/classes../lib/*
# echo $CLASSPATH
java -classpath $CLASSPATH net.fereira.gps.GPX2Text $*
