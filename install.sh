#!/bin/bash
#mvn clean install -Drevision='0.1' -f pom.xml

# if maven not found javadoc -> set directly JAVA_HOME environment
#JAVA_HOME=/usr/lib/jvm/java-11-openjdk-amd64/ mvn install

JAVA_HOME=/usr/lib/jvm/java-11-openjdk-amd64/ mvn clean install -f pom.xml

