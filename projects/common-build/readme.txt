Contained in this directory is the Spring Jumpstart common build system.

It is ant 1.6 based and uses Ivy for dependency management.

Projects are expected to import master build files contained within 
this directory as needed for the build targets they require.

Build targets are organized into logical files:
- common-targets.xml : core targets applicable to all projects
- clover-targets.xml : for working with clover
- tomcat-targets.xml : for deploying webapps to the tomcat servlet container
etc...

As an example, here is Spring Web Flow's project build.xml:

<project name="spring-webflow" default="dist">

  <property file="build.properties"/>
  <property file="project.properties"/>
  <property file="${common.build.dir}/build.properties"/>
  <property file="${common.build.dir}/project.properties"/>
  <property file="${user.home}/build.properties"/>

  <property name="project.title" value="Spring Web Flow"/>
  <property name="project.package" value="org.springframework.webflow"/>
  
  <import file="${common.build.dir}/common-targets.xml"/>

  <import file="${common.build.dir}/clover-targets.xml"/>

</project>

This build.xml imports the "common-targets.xml" fragment containing
core targets for compilation, distribution unit creation, and junit 
testing.  It also imports "clover-targets.xml" to facilitate the 
generation of test coverage reports with clover.

* Custom resolver update (costin)

It is likely that each project will want to have its own dependencies that may
not be available on the main resolvers (ibiblio). In this case one can use
custom-resolver.xml file (by copying inside the the project root) and define her own resolver; there are no restriction
on the type (ibiblio, chain, etc...) but the name has to be "custom". The resolver will be appended
to the default list of resolvers.
