package org.springmodules.hivemind;

import java.io.IOException;
import java.net.URL;
import java.util.Locale;

import org.apache.hivemind.Resource;
import org.apache.hivemind.util.FileResource;
import org.springframework.context.ApplicationContextException;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.UrlResource;

/**
 * @author Thierry Templier
 */
public class DefaultResourceWrapper extends FileResource {
	private org.springframework.core.io.Resource resource;

	public DefaultResourceWrapper(org.springframework.core.io.Resource resource) {
		super(resource.getFilename());
		this.resource=resource;
	}

	/**
	 * @see org.apache.hivemind.Resource#getLocalization(java.util.Locale)
	 */
	public Resource getLocalization(Locale arg0) {
		return super.getLocalization(arg0);
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
	protected Resource newResource(String arg0) {
		try {
			return new DefaultResourceWrapper(this.resource.createRelative(arg0));
		} catch(IOException ex) {
			throw new ApplicationContextException("Error during creation of a new resource");
		}
	}

}
