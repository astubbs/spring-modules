package org.springmodules.hivemind;

import java.io.IOException;
import java.net.URL;
import java.util.Locale;

import org.apache.hivemind.ClassResolver;
import org.apache.hivemind.Resource;
import org.apache.hivemind.util.ClasspathResource;
import org.springframework.context.ApplicationContextException;
import org.springframework.core.io.ClassPathResource;

/**
 * @author Thierry Templier
 */
public class ClasspathResourceWrapper extends ClasspathResource {
	private ClassResolver classResolver;
	private ClassPathResource resource;

	public ClasspathResourceWrapper(ClassResolver classResolver,ClassPathResource resource) {
		super(classResolver,resource.getFilename());
		this.classResolver=classResolver;
		this.resource=resource;
	}

	/**
	 * @see org.apache.hivemind.Resource#getLocalization(java.util.Locale)
	 */
	public Resource getLocalization(Locale locale) {
		return super.getLocalization(locale);
	}

	/**
	 * @see org.apache.hivemind.Resource#getResourceURL()
	 */
	public URL getResourceURL() {
		try {
			return resource.getURL();
		} catch(IOException ex) {
			throw new ApplicationContextException("Error during getting the url of the resource");
		}
	}

	/**
	 * @see org.apache.hivemind.util.AbstractResource#newResource(java.lang.String)
	 */
	protected Resource newResource(String name) {
		return new ClasspathResourceWrapper(classResolver,
						(ClassPathResource)this.resource.createRelative(name));
	}
}
