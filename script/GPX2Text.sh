#!/bin/bash
export CLASSPATH=../target/classes:../lib/*
java -classpath ../target/classes:../lib/* net.nypc.gps.GPX2Text $*
