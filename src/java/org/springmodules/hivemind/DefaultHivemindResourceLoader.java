package org.springmodules.hivemind;

import org.apache.hivemind.ClassResolver;
import org.apache.hivemind.Resource;
import org.apache.hivemind.impl.DefaultClassResolver;
import org.apache.hivemind.util.ClasspathResource;
import org.apache.hivemind.util.FileResource;

/**
 * @author Thierry TEMPLIER
 */
public class DefaultHivemindResourceLoader implements HivemindResourceLoader {
	private ClassResolver classResolver;

	public DefaultHivemindResourceLoader(ClassResolver classResolver) {
		this.classResolver=classResolver;
	}

	/**
	 * @see org.springmodules.hivemind.HivemindResourceLoader#getResource(java.lang.String)
	 */
	public Resource getResource(String location) {
		if( location.startsWith(CLASSPATH_URL_PREFIX)
		               || location.startsWith(WEBAPP_URL_PREFIX) ) {
			String locationWithoutPrefix=location.substring(CLASSPATH_URL_PREFIX.length());
			return new ClasspathResource(classResolver,locationWithoutPrefix);
		} else {
			return new FileResource(location);
		}
	}

}
