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
import org.springmodules.lucene.search.factory.SearcherFactory;
import org.springmodules.lucene.search.factory.SimpleSearcherFactory;

/**
 * Simple factory bean to configure a factory to manipulate an index.
 * 
 * @author Thierry Templier
 */
public class SimpleIndexFactoryBean implements FactoryBean,InitializingBean {

	private SimpleIndexFactory factory;
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
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	public void afterPropertiesSet() throws Exception {
		if (getDirectory() == null) {
			throw new IllegalArgumentException("directory is required");
		}
		factory=new SimpleIndexFactory(getDirectory(),getAnalyzer());
	}

	/**
	 * @return
	 */
	public Directory getDirectory() {
		return directory;
	}

	/**
	 * @param directory
	 */
	public void setDirectory(Directory directory) {
		this.directory = directory;
	}

	/**
	 * @return
	 */
	public Analyzer getAnalyzer() {
		return analyzer;
	}

	/**
	 * @param analyzer
	 */
	public void setAnalyzer(Analyzer analyzer) {
		this.analyzer = analyzer;
	}

}
