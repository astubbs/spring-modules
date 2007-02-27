=======================================================
== Spring/db4o sample application ==
=======================================================

@author Costin Leau


1. MOTIVATION

The aim of the example is to show how to use the support
of db4o in Spring Modules.
The sample shows how the components can be created, wired and 
execute a couple of operations on the db4o database.


2. BUILD AND DEPLOYMENT

This directory contains the standalone app source.
For deployment, it needs to be built with Apache Ant.
The only requirements are JDK >=1.3 and Ant >=1.5.

Run "ant -p" in this directory for available targets (e.g. "ant build",
"ant run"). Note that to start Ant this way, you'll need an XML parser
in your classpath (e.g. in "%JAVA_HOME%/jre/lib/ext"; included in JDK 1.4).

To run the project run " ant clean-all compile run"