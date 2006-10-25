package org.springmodules.ant.task;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.util.FileUtils;

/**
 * Borrowed from cenqua (the cenquatasks jar is not in maven for some reason, 
 * so it is easier to include a copy of this very simple task here).
 * 
 * @author Dave Syer
 *
 */
public class ExtendClasspathTask extends Task {


	    public ExtendClasspathTask()
	    {
	        paths = new ArrayList();
	        try
	        {
	            addURLMethod = (java.net.URLClassLoader.class).getDeclaredMethod("addURL", new Class[] {
	                java.net.URL.class
	            });
	        }
	        catch(NoSuchMethodException e)
	        {
	            throw new BuildException("Unable to setup classpath extender", e);
	        }
	        addURLMethod.setAccessible(true);
	    }

	    private void addURL(URLClassLoader ucl, URL path)
	        throws BuildException
	    {
	    	Exception caught = null;
	        try {
				addURLMethod.invoke(ucl, new Object[] {
				    path
				});
			} catch (IllegalArgumentException e) {
				caught = e;
			} catch (IllegalAccessException e) {
				caught = e;
			} catch (InvocationTargetException e) {
				caught = e;
			}
			if (caught!=null) {
				throw new BuildException("Unable to load class path at URL="+path, caught);
			}
	    }

	    public void execute()
	    {
	        ClassLoader loader;
	        for(loader = getProject().getClass().getClassLoader(); !(loader instanceof URLClassLoader);)
	        {
	            loader = loader.getParent();
	            if(loader == null)
	                throw new BuildException("Unable to find a URLClassLoader to which path may be added");
	        }

	            FileUtils fu = FileUtils.newFileUtils();
	            for(Iterator iterator = paths.iterator(); iterator.hasNext();)
	            {
	                Path path = (Path)iterator.next();
	                String pathElements[] = path.list();
	                int i = 0;
	                while(i < pathElements.length) 
	                {
	                    String pathElement = pathElements[i];
	                    URL url;
						try {
							url = new URL(fu.toURI(pathElement));
						} catch (MalformedURLException e) {
							throw new BuildException("Unable to load class path at path="+pathElement, e);
						}
	                    List urls = Arrays.asList(((URLClassLoader)loader).getURLs());
	                    if (!urls.contains(url)) {
	                    	addURL((URLClassLoader)loader, url);
	                    }
	                    i++;
	                }
	            }

	    }

	    public void addPath(Path path)
	    {
	        paths.add(path);
	    }

	    public void setPath(String path)
	    {
	        paths.add(new Path(getProject(), path));
	    }

	    private List paths;
	    private Method addURLMethod;

}
