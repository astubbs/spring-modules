/*
 * Copyright 2002-2006 the original author or authors.
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
package org.springmodules.commons.chain;

import java.util.Iterator;
import java.util.Map;

import org.apache.commons.chain.Catalog;
import org.apache.commons.chain.Command;
import org.apache.commons.chain.impl.CatalogBase;
import org.springframework.beans.factory.config.AbstractFactoryBean;

/**
 * Simple FactoryBean used for simplifying Catalog creation from Spring
 * application context XML.
 * 
 * @author Costin Leau
 * 
 */
public class CatalogFactoryBean extends AbstractFactoryBean {

	private Map commands;
	private Catalog catalog;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.beans.factory.config.AbstractFactoryBean#createInstance()
	 */
	protected Object createInstance() throws Exception {
		return new CatalogBase();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.beans.factory.FactoryBean#getObjectType()
	 */
	public Class getObjectType() {
		return (catalog == null ? Catalog.class : catalog.getClass());
	}

	public Map getCommands() {
		return commands;
	}

	/**
	 * Add the map of commands to the created catalog, using the key as the name
	 * and the value as the command.
	 * 
	 * @param catalogs
	 */
	public void setCommands(Map commands) {
		this.commands = commands;
	}

	public void afterPropertiesSet() throws Exception {
		super.afterPropertiesSet();

		if (commands == null)
			throw new IllegalArgumentException("commands parameter is required");

		for (Iterator iter = commands.entrySet().iterator(); iter.hasNext();) {
			Map.Entry mapEntry = (Map.Entry) iter.next();
			catalog.addCommand((String) mapEntry.getKey(), (Command) mapEntry.getValue());
		}
	}
}
