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

package org.springmodules.lucene.index.support;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.store.Directory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springmodules.lucene.index.factory.IndexFactory;
import org.springmodules.lucene.index.factory.SimpleIndexFactory;

/**
 * Simple factory bean to configure a simple factory to manipulate
 * an index.
 * 
 * @author Thierry Templier
 * @see org.springmodules.lucene.index.factory.SimpleIndexFactory
 */
public class SimpleIndexFactoryBean implements FactoryBean,InitializingBean {

	private SimpleIndexFactory factory;
	private boolean resolveLock = false;
	private boolean create = false;
	private Directory directory;
	private Analyzer analyzer;

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
		return IndexFactory.class;
	}

	/**
	 * @see org.springframework.beans.factory.FactoryBean#isSingleton()
	 */
	public boolean isSingleton() {
		return true;
	}

	/**
	 * Set the Lucene Directory used by the IndexFactory.
	 */
	public void setDirectory(Directory directory) {
		this.directory = directory;
	}

	/**
	 * Return the Lucene Directory used by the IndexFactory.
	 */
	public Directory getDirectory() {
		return directory;
	}

	/**
	 * Set if the index locking must be resolved
	 */
	public void setResolveLock(boolean b) {
		resolveLock = b;
	}

	/**
	 * Return if the index locking must be resolved
	 */
	public boolean isResolveLock() {
		return resolveLock;
	}

	/**
	 * Set if the index must be created if it don't exist
	 */
	public void setCreate(boolean create) {
		this.create = create;
	}

	/**
	 * Return if the index must be created if it don't exist
	 */
	public boolean isCreate() {
		return create;
	}

	/**
	 * Set the Lucene Analyzer used by the IndexFactory.
	 */
	public void setAnalyzer(Analyzer analyzer) {
		this.analyzer = analyzer;
	}

	/**
	 * Return the Lucene Analyzer used by the IndexFactory.
	 */
	public Analyzer getAnalyzer() {
		return analyzer;
	}

	/**
	 * This method constructs a SimpleIndexFactory instance based on
	 * the configured directory and analyzer.
	 */
	public void afterPropertiesSet() throws Exception {
		if (getDirectory() == null) {
			throw new IllegalArgumentException("directory is required");
		}
		this.factory = new SimpleIndexFactory(getDirectory(), getAnalyzer());
		this.factory.setResolveLock(resolveLock);
		this.factory.setCreate(create);
	}

}
