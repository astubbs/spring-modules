package org.springmodules.hivemind;

import org.apache.hivemind.ClassResolver;
import org.apache.hivemind.Resource;

/**
 * Interface to be implemented by objects that can load Hivemind
 * resources from different places.
 *
 * @author Thierry Templier
 */
public interface HivemindResourceLoader {

	/** Pseudo URL prefix for loading from the class path: "classpath:" */
	String CLASSPATH_URL_PREFIX = "classpath:";

	/** Pseudo URL prefix for loading from the file system: "file:" */
	String FILE_SYSTEM_URL_PREFIX = "file:";

	/** Pseudo URL prefix for loading from a webapp: "/WEB-INF" */
	String WEBAPP_URL_PREFIX = "/WEB-INF";

	/**
	 * Return a Hivemind Resource handle for the specified resource.
	 * <p><ul>
	 * <li>Must support fully qualified URLs, e.g. "file:C:/hivemind-configuration.xml".
	 * <li>Must support classpath pseudo-URLs, e.g. "classpath:hivemind-configuration.xml".
	 * <li>Should support relative file paths, e.g. "WEB-INF/hivemind-configuration.xml".
	 * @param location the resource location
	 * @return a corresponding Resource handle
	 */
	Resource getResource(String location);

}
