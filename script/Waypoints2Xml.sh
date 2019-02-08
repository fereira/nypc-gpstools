#!/bin/bash

export CLASSPATH=target/classes/:../lib/*
java -classpath $CLASSPATH net.nypc.gps.Waypoints2Xml $*
