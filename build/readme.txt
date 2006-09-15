Doing  a release
--------------------

1. update build/build.properties and /docs/reference/src/index-module.xml with the release dates
    update common-build/project.properties (w/ release name (0.x))
     
2. test that everything is fine
 ant clean-all release
 see target/ folder
   -- check that the docs are properly generated (including html and pdf)
   -- check packages, including javadocs, sources and changelog
   
 3. upload the jar without the full distro
 4. log into the jteam machine, add the distro and then upload it to java.net
 5. create a new version on JIRA and close the existing one
 6. do the version release on JIRA
 7. commit the 0.x docs on the CVS website
 7.5. do a CVS tag
 8. update the project page
 
 9. post the announcement on the forum and then on Spring Framework site.
 
   
