#!/bin/sh 

export JAVA_HOME=/usr/lib/jvm/java-6-sun
export HSQLDB_LIB=../WebContent/WEB-INF/lib/hsqldb.jar

java -classpath ../WebContent/WEB-INF/lib/hsqldb.jar org.hsqldb.Server -database.0 test
