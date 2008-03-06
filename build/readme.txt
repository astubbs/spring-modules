Doing  a release
--------------------
1. update build/build.properties and /docs/reference/src/index-module.xml with the release dates
    update projects/common-build/project.properties (w/ release name (0.x))
     
2. test that everything is fine
 ant clean-all release
 see target/ folder
   -- check that the docs are properly generated (including html and pdf)
   -- check packages, including javadocs, sources and changelog
   
3. upload the jar without the full distro
5. create a new version on JIRA and close the existing one
6. do the version release on JIRA
7. commit the 0.x docs on the CVS website
7.5. do a CVS tag
7.8. update the version in the various files (see step 1) to 0.x+1-DEV
8. update the project page
 
9. post the announcement on the forum and then on Spring Framework site.
 
   
