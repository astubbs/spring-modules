=========================================
== Spring/Hivemind sample application  ==
=========================================

@author Thierry Templier


1. MOTIVATION

The aim of the sample is to show how to use the support
of Hivemind 1.0 in Spring Modules.
It uses an explicit configuration of the Hivemind services
based on the facilities of Spring to located the xml
configuration file.
This file (configuration.xml) uses the sub module feature
of Hivemind to define the configuration in several xml
files.


2. BUILD AND DEPLOYMENT

This directory contains the standalone app source.
For deployment, it needs to be built with Apache Ant.
The only requirements are JDK >=1.3 and Ant >=1.5.

Run "build.bat" in this directory for available targets (e.g. "build.bat build",
"build.bat launch"). Note that to start Ant this way, you'll need an XML parser
in your classpath (e.g. in "%JAVA_HOME%/jre/lib/ext"; included in JDK 1.4).

