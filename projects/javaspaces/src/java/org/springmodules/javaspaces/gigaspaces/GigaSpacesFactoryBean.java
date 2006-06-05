/*
 * Copyright 2006 GigaSpaces Technologies. All rights reserved.
 *
 * THIS SOFTWARE IS PROVIDED "AS IS," WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED INCLUDING BUT NOT LIMITED TO WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE OR
 * NON-INFRINGEMENT. GIGASPACES WILL NOT BE LIABLE FOR ANY DAMAGE OR
 * LOSS IN CONNECTION WITH THE SOFTWARE.
 */
package org.springmodules.javaspaces.gigaspaces;

import net.jini.space.JavaSpace;

import org.springmodules.javaspaces.AbstractJavaSpaceFactoryBean;

import com.j_spaces.core.client.SpaceFinder;

/**
 * A FactoryBean class which creates a singleton JavaSpace using
 * the Gigaspaces API. For the creation of the space, the urls property must be specified.
 *
 * Copyright:    Copyright 2006 GigaSpaces Technologies. All rights reserved.
 * Company:      Gigaspaces Technologies
 * @author       Lior Ben Yizhak
 * @version      5.0
 *
 */
public class GigaSpacesFactoryBean extends AbstractJavaSpaceFactoryBean {

	private String[] urls;


	/**
	 * @see org.springmodules.javaspaces.AbstractJavaSpaceFactoryBean#createSpace()
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
	 * @see org.springmodules.javaspaces.AbstractJavaSpaceFactoryBean#afterPropertiesSet()
	 */
	public void afterPropertiesSet() throws Exception {
		super.afterPropertiesSet();
		if (urls == null)
			throw new IllegalArgumentException("urls property is required");
	}
}
