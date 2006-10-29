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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

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
	private Map properties = new HashMap(10);

	/**
	 * @see org.springmodules.javaspaces.AbstractJavaSpaceFactoryBean#createSpace()
	 */
	protected JavaSpace createSpace() throws Exception {
		if(properties.isEmpty()){
			return (JavaSpace) SpaceFinder.find(urls);
		}else{
			return (JavaSpace) SpaceFinder.find(getUrlsAsString(),getUrlProperties());
		}
	}

	/**
	 * @return Returns the urls.
	 */
	public String[] getUrls() {
		return urls;
	}

	/**
	 * @return Returns the urls as one string which separated with comma delimiters.
	 */
	private String getUrlsAsString() {
		StringBuilder sb = new StringBuilder();
		for(int ii = 0; ii < urls.length; ii++){
			sb.append(urls[ii]);
			if(ii < (urls.length - 1)){
				sb.append(";");
			}
		}
		return sb.toString();
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

	/**
	 * Create URL properties object for space finder from the hashmap
	 * @return properties for space finder
	 */
	private Properties getUrlProperties()
	{
		Properties urlPproperties = new Properties();
		Iterator iterator = properties.keySet().iterator();
		while(iterator.hasNext()){
			Object objectKey = iterator.next();
			urlPproperties.put(objectKey, properties.get(objectKey));
		}
		return urlPproperties;
	}

	/**
	 * Get properties of the space URL.
	 * @return the properties.
	 */
	public Map getProperties()
	{
		return properties;
	}

	/**
	 * Set properties for the space URL.
	 * @param the properties.
	 */
	public void setProperties(Map properties)
	{
		this.properties = properties;
	}

}
