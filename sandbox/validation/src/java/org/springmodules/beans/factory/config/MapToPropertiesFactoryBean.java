/*
 * Copyright 2004-2005 the original author or authors.
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
package org.springmodules.beans.factory.config;

import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * <p>
 * Factory bean that converts a map to a properties instance.
 * 
 * <p>
 * This factory bean will fail is not all keys and values of the map are
 * strings.
 * 
 * 
 * @author Steven Devijver
 * @since Aug 6, 2005
 */
public class MapToPropertiesFactoryBean implements FactoryBean,
		InitializingBean {

	private Map map = null;
	private Properties properties = null;
	
	public MapToPropertiesFactoryBean() {
		super();
	}

	public void setMap(Map map) {
		this.map = map;
	}
	
	public void afterPropertiesSet() throws Exception {
		this.properties = new Properties();
		if (this.map != null) {
			for (Iterator iter = this.map.entrySet().iterator(); iter.hasNext();) {
				Map.Entry entry = (Map.Entry)iter.next();
				String key = null;
				String value = null;
				if (entry.getKey() == null) {
					throw new IllegalArgumentException("Key is null!");
				} else if (!(entry.getKey() instanceof String)) {
					throw new IllegalArgumentException("This key is not a string value: [" + entry.getKey() + "]!");
				} else {
					key = (String)entry.getKey();
				}
				if (entry.getValue() == null) {
					value = "";
				} else if (!(entry.getValue() instanceof String)) {
					throw new IllegalArgumentException("This value is not a string value [" + entry.getValue() + "]!");
				} else {
					value = (String)entry.getValue();
				}
				properties.setProperty(key, value);
			}
		}
	}

	public Object getObject() throws Exception {
		return this.properties;
	}

	public Class getObjectType() {
		return Properties.class;
	}

	public boolean isSingleton() {
		return true;
	}
}