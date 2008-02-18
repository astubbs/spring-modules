Note, this project does *not* depend on the db4o project, rather it depends upon the
spring-modules-xxx.jar project.  This means that any changes made in the db4o eclipse 
project will *not* be picked up by this project until 'ant dist' has been run in the
springmodules/projects/db4o project.

This is because the commercial db4o jar is called db4o.jar, as is the eclipse project.
This means that only one of them can be on the classpath, resulting in ClassNotFound 
exceptions.