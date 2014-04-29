#!/bin/bash

# Uncomment and set in not set by the calling environment
#JAVA_HOME=

# JVM arguments - remote debug argument
JAVA_OPTS="$JAVA_OPTS -Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=5005"
 
# JVM arguments - GC options
JAVA_OPTS="$JAVA_OPTS -XX:+UseParNewGC -XX:+UseConcMarkSweepGC"

# JVM arguments - print startup JVM argument
JAVA_OPTS="$JAVA_OPTS -XX:+PrintCommandLineFlags"

# JVM arguments - application configuration and jetty system properties  
JAVA_OPTS="$JAVA_OPTS -Dconfiguration=production -Djetty.home=. -Djetty.logs=./logs"

# Start Server
"$JAVA_HOME/bin/java" $JAVA_OPTS -jar launcher-1.0-SNAPSHOT.jar


