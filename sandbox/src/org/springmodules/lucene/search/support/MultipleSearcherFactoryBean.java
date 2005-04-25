/*
 * Copyright 2002-2005 the original author or authors.
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

package org.springmodules.lucene.search.support;

import java.util.List;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springmodules.lucene.search.factory.MultipleSearcherFactory;
import org.springmodules.lucene.search.factory.SearcherFactory;

/**
 * Factory bean to configure a factory to make search on several
 * indexes.
 * 
 * @author Thierry Templier
 */
public class MultipleSearcherFactoryBean implements FactoryBean,InitializingBean {

	private MultipleSearcherFactory factory;

	/**
	 * @see org.springframework.beans.factory.FactoryBean#getObject()
	 */
	public Object getObject() throws Exception {
		return factory;
	}

	/**
	 * @see org.springframework.beans.factory.FactoryBean#getObjectType()
	 */
	public Class getObjectType() {
		return SearcherFactory.class;
	}

	/**
	 * @see org.springframework.beans.factory.FactoryBean#isSingleton()
	 */
	public boolean isSingleton() {
		return true;
	}

	/**
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	public void afterPropertiesSet() throws Exception {
		if (getDirectories() == null) {
			throw new IllegalArgumentException("at least one directory is required");
		}
	}

	/**
	 * @return
	 */
	public List getDirectories() {
		return this.factory!=null ? this.factory.getDirectories() : null;
	}

	/**
	 * @param directory
	 */
	public void setDirectories(List directories) {
		this.factory = new MultipleSearcherFactory(directories);
	}
}
