package org.springmodules.javaspaces.giga;

import net.jini.space.JavaSpace;

import org.springmodules.javaspaces.AbstractJavaSpaceFactoryBean;

import com.j_spaces.core.client.SpaceFinder;

/**
 * FactoryBean for retrieving a GigaSpace.
 * 
 * @author Costin Leau
 *  
 */
public class GigaSpacesFactoryBean extends AbstractJavaSpaceFactoryBean {

	private String[] urls;

	
	/**
	 * @see org.springmodules.spaces.AbstractJavaSpaceFactoryBean#createSpace()
	 */
	protected JavaSpace createSpace() throws Exception {
		return (JavaSpace) SpaceFinder.find(urls);
	}

	/**
	 * @return Returns the urls.
	 */
	public String[] getUrls() {
		return urls;
	}

	/**
	 * @param urls The urls to set.
	 */
	public void setUrls(String[] urls) {
		this.urls = urls;
	}

	/**
	 * @see org.springmodules.spaces.AbstractJavaSpaceFactoryBean#afterPropertiesSet()
	 */
	public void afterPropertiesSet() throws Exception {
		super.afterPropertiesSet();
		if (urls == null)
			throw new IllegalArgumentException("urls property is required");
	}
}
