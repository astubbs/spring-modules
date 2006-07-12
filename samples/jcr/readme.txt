=======================================================
== Spring/Java Content Repository sample application ==
=======================================================

@author Costin Leau


1. MOTIVATION

The aim of the sample is to show how to use the support
of JCR (Java Content Repository) 1.0 in Spring Modules.
Besides generic functionality specific to the JCR, support 
for specific implementations (like Jackrabbit and Jeceira) is also provided.
The sample shows how the components can be created, wired and 
execute a couple of operations on the created repository.
Both Jeceira and Jackrabbit are created but as default the Jackrabbit 
repository is used.


2. BUILD AND DEPLOYMENT

This directory contains the standalone app source.
For deployment, it needs to be built with Apache Ant.
The only requirements are JDK >=1.3 and Ant >=1.5.

Run "ant -p" in this directory for available targets (e.g. "ant build",
"ant run"). Note that to start Ant this way, you'll need an XML parser
in your classpath (e.g. in "%JAVA_HOME%/jre/lib/ext"; included in JDK 1.4).

To run the project run " ant clean-all compile run"

3. JECEIRA native libraries.

Jeceira uses native libraries for generating unique identifiers which have to be in the
classpath under jug-native library. If you have problems make sure you read the README.TXT
found in the library directory.
