=========================================
== Spring/Hivemind sample application  ==
=========================================

@author Thierry Templier


1. MOTIVATION

The aim of the sample is to show how to use the support
of JSR94 1.0 in Spring Modules with JRules as rule engine.

This sample uses rules to find the good bargain cars in
a database.

2. BUILD AND DEPLOYMENT

This directory contains the standalone app source.
For deployment, it needs to be built with Apache Ant.
The only requirements are JDK >=1.3 and Ant >=1.5.

Run "build.bat" in this directory for available targets (e.g. "build.bat build",
"build.bat launch"). Note that to start Ant this way, you'll need an XML parser
in your classpath (e.g. in "%JAVA_HOME%/jre/lib/ext"; included in JDK 1.4).

You need to start the hsqldb database before launch the sample
application ("build.bat starthsqldb").

As JRules is not a free product, the library jar
files aren't include in the sample.
To execute it, you need to have installed JRules 5.0 and
put the following jar files in the classpath of this sample.
The ant file automatically add the following needed jars in
the execution classpath.
These files are:
  - jrules-engine.jar
  - jrules-jsr94.jar
  - sam.jar

The JRules rules are written in irl (Ilog Rule Language). There are
two: one without ruleflow and one with.