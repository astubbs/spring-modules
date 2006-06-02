==================================================
== Spring/Commons Validator sample application  ==
==================================================

@author Thierry Templier


1. MOTIVATION

The aim of the sample is to show how to use the support
of Commons Validator in Spring Modules to validate fields
of HTML forms.

This sample implements a simple form controller based on the
SimpleFormController class of Spring.

Note that the sample uses the implementation of validwhen rules
of Adam Kramer.
For more informations, see the following issues:
MOD-38 (http://opensource2.atlassian.com/projects/spring/browse/MOD-38)
MOD-49 (http://opensource2.atlassian.com/projects/spring/browse/MOD-49)

The configuration files of Commons Validator are validator-rules.xml
and validator.xml in the war/WEB-INF directory.


2. BUILD AND DEPLOYMENT

This directory contains the standalone app source.
For deployment, it needs to be built with Apache Ant.
The only requirements are JDK >=1.3 and Ant >=1.5.

Run "build.bat" in this directory for available targets (e.g. "build.bat build",
"build.bat launch"). Note that to start Ant this way, you'll need an XML parser
in your classpath (e.g. in "%JAVA_HOME%/jre/lib/ext"; included in JDK 1.4).

In order to build the war file, simply run "build.bat war". Then you 
only need to develop this file on your application server.


3. WEB APPLICATION

To use the web application, you can use the following uri:
- http://<server>:<port>/<context>/person.html. This shows
  the form and you can enter values in the fields to test
  the validation of rules contained in the validator.xml file.