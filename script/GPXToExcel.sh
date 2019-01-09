#!/bin/bash
export CLASSPATH=../build:../lib/*
java -classpath ../build:../lib/* net.nypc.gps.GPXToExcel $*
