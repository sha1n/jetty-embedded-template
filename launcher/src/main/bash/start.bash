#!/bin/bash

# Uncomment and set in not set by the calling environment
#JAVA_HOME=

# Set JVM argument (addative)
JAVA_OPTS="$JAVA_OPTS -Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=5005 -XX:+PrintCommandLineFlags -XX:+UseParNewGC -XX:+UseConcMarkSweepGC"

# Set Java system properties
JAVA_OPTS="$JAVA_OPTS -Dconfiguration=production -Djetty.home=. -Djetty.logs=./logs"

# Start Server
"$JAVA_HOME/bin/java" $JAVA_OPTS -jar launcher-1.0-SNAPSHOT.jar


