FEED-XT
-------

1. Introduction

Feed-XT is a sample application built on the Spring Framework 2.0 and the Spring Modules XT Framework 0.9.

It is a simple, yet fully functional, web based feed reader with the following features:

* Users management (signup/login).
* Feed subscriptions management (add/remove).
* Integrated feed visualization. 
* Ajax-enabled, thanks to the XT Ajax Framework!

2. Requirements

Feed-XT requires:

* JDK 1.5 or higher.
* Apache Ant 1.6 or higher.
* Apache Tomcat 5.5.X.

Application data is transparently stored by using the DB4O ODBMS through the Spring Modules DB4O Integration, so you don't need any pre-installed RDBMS. 

3. Configuration

You have to configure two simple parameters:

* Your Apache Tomcat install directory, in the FEED-XT_HOME/build.properties file.
* Your DB4O database file name and location, in the FEED-XT_HOME/src/etc/resources/application.properties file.

4. Build and Deployment

Feed-XT uses the Apache Ant and Ivy combo build system, featuring automatic dependencies resolution. 

To build Feed-XT, just type "ant" in your console under the Feed-XT home directory; you'll need an active internet connection, because Ivy will automatically download and install Feed-XT dependencies.
To deploy it, just start your Apach Tomcat server, go under the Feed-XT home directory and type "ant tomcat-redeploy".
To enjoy it .. point your browser to "http://your_host:your_port/feed-xt" ... and that's all!
