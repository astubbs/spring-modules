=========================================
== DB4O-Spring sample application      ==
=========================================

@author Daniel Mitterdorfer/Costin Leau

1. Introduction

This is a minimal sample application that demonstrates how to use and configure
db4o-spring in a web environment.

2. Build and Deployment

This directory contains the web app source.
For deployment, it needs to be built with Apache Ant.
The only requirements are JDK >=1.3 and Ant >=1.5.

Run "build.bat" in this directory for available targets (e.g. "build.bat build",
"build.bat warfile"). Note that to start Ant this way, you'll need an XML parser
in your classpath (e.g. in "%JAVA_HOME%/jre/lib/ext"; included in JDK 1.4).

You can also invoke an existing installation of Ant, with this directory
as execution directory. Note that you need to do this to execute the "test"
target, as you need the JUnit task from Ant's optional.jar (not included).

Deploy the war file to your J2EE server (e.g. through Tomcat's manager console) 
and point your browser to http://localhost:8080/recipemanager. A welcome screen 
should appear.

