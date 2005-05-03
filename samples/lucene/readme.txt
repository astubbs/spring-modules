=========================================
== Spring/Lucene sample application  ==
=========================================

@author Thierry Templier


1. MOTIVATION

The aim of the sample is to show how to use the support
of Lucene in Spring Modules to index documents and datas
from a database but too, to search in a standalone or a
web mode.

This sample uses some datas preloaded in an Hypersonic
database and files in a specified directory. You can added
documents in it or change it to make more tests.

Before beginning the sample application, you need to specify
in the applicationContext.xml the location of the index
directory. The default one could be not existing on the
system.

2. BUILD AND DEPLOYMENT

This directory contains the standalone app source.
For deployment, it needs to be built with Apache Ant.
The only requirements are JDK >=1.3 and Ant >=1.5.

Run "build.bat" in this directory for available targets (e.g. "build.bat build",
"build.bat launch"). Note that to start Ant this way, you'll need an XML parser
in your classpath (e.g. in "%JAVA_HOME%/jre/lib/ext"; included in JDK 1.4).

You need to start the hsqldb database before launch the sample
application ("build.bat starthsqldb").