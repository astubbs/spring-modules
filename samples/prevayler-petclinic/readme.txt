=========================================
== Spring PetClinic sample application ==
=========================================

@author Ken Krebs
@author Juergen Hoeller
@author Rob Harrop
@author Sergio Bossa


1. ABOUT

This is a modified PetClinic implementation has the following features:

* Prevayler (http://prevayler.codehaus.org) based implementation of the data access strategy, 
  using the Spring Modules Prevayler-Template.

To access the original PetClinic application, please download a Spring Framework distribution (www.springframework.org) and go into its samples directory.


2. BUILD AND DEPLOYMENT

This directory contains the web app source.
For deployment, it needs to be built with Apache Ant.
The only requirements are JDK >=1.3 and Ant >=1.5.

Moreover, you need to copy Prevayler-Template library, with its dependencies, into the lib/prevayler-petclinic directory.

Additional documentation can be found in the file "petclinic.html" which is
in the "war/html" directory. This file is available in the running application
through the "Tutorial" link.

