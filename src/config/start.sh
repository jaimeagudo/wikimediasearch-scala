#!/bin/sh

JAVA_HOME=/home/java

# This script will start wikimediasearch

PIDFILE=wikimediasearch.pid

if [ -e $PIDFILE ] ; then

echo "wikimediasearch already started."

else


CLASSPATH=./wikimediasearch-0.3.0.jar:./lib/*


    java -Xms64m -Xmx4500m -XX:+HeapDumpOnOutOfMemoryError -Dlogger.file=./resources/logback.xml -cp $CLASSPATH  com.vegatic.wikimediasearch.Boot &

PID=$!

echo $PID > $PIDFILE

wait $PID

rm -f $PIDFILE

fi