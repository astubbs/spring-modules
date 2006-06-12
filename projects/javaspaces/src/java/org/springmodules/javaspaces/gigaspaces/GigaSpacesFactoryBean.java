/*
* Copyright 2006 GigaSpaces, Inc.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*      http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
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
