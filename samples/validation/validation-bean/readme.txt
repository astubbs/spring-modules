=================================================================
== springmodules Bean Validation Framework sample application  ==
=================================================================

@author Thierry Templier/Uri Boness


1. MOTIVATION

The aim of the sample is to show how to use the BeanValidation
Framework in Spring Modules to validate fields of HTML forms.

This sample implements a simple form controller based on the
SimpleFormController class of Spring.


2. BUILD AND DEPLOYMENT

This directory contains the standalone app source.
For deployment, it needs to be built with Apache Ant.
The only requirements are JDK >= 1.3 (if annotations are used)
or JDK >= 5 (if xml configuration is used) and Ant >=1.5.


3. WEB APPLICATION

To use the web application, you can use the following uri:
- http://<server>:<port>/<context>/person.html. This shows
  the form and you can enter values in the fields to test
  the validation.