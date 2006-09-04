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
import org.apache.commons.chain.CatalogFactory;
import org.apache.commons.chain.impl.CatalogFactoryBase;
import org.springframework.beans.factory.config.AbstractFactoryBean;

/**
 * Simple FactoryBean used for simplifying CatalogFactory creation from Spring
 * application context XML.
 * 
 * @author Costin Leau
 * 
 */
public class CatalogFactoryFactoryBean extends AbstractFactoryBean {

	private CatalogFactory catalogFactory;
	private Map catalogs;
	private Catalog defaultCatalog;

	public Catalog getDefaultCatalog() {
		return defaultCatalog;
	}

	public void setDefaultCatalog(Catalog defaultCatalog) {
		this.defaultCatalog = defaultCatalog;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.beans.factory.config.AbstractFactoryBean#createInstance()
	 */
	protected Object createInstance() throws Exception {
		return new CatalogFactoryBase();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.beans.factory.FactoryBean#getObjectType()
	 */
	public Class getObjectType() {
		return (catalogFactory == null ? CatalogFactory.class : catalogFactory.getClass());
	}

	public Map getCatalogs() {
		return catalogs;
	}

	/**
	 * Add the map of catalogs to the catalog factory, using the key as the name and the value
	 * as the catalog.
	 * 
	 * @param catalogs
	 */
	public void setCatalogs(Map catalogs) {
		this.catalogs = catalogs;
	}

	public void afterPropertiesSet() throws Exception {
		if (defaultCatalog == null && catalogs == null)
			throw new IllegalArgumentException("the defaultCatalog or catalogs have to be specified");

		super.afterPropertiesSet();

		if (catalogs != null)
			for (Iterator iter = catalogs.entrySet().iterator(); iter.hasNext();) {
				Map.Entry mapEntry = (Map.Entry) iter.next();
				catalogFactory.addCatalog((String) mapEntry.getKey(), (Catalog) mapEntry.getValue());
			}
		
		if (defaultCatalog != null)
			catalogFactory.setCatalog(defaultCatalog);
	}

}
