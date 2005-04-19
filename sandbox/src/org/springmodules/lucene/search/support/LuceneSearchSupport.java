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

import org.apache.lucene.analysis.Analyzer;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.InitializingBean;
import org.springmodules.lucene.search.core.LuceneSearchTemplate;
import org.springmodules.lucene.search.factory.SearcherFactory;

/**
 * @author Thierry Templier
 */
public abstract class LuceneSearchSupport implements InitializingBean {
	private LuceneSearchTemplate template;
	private SearcherFactory searcherFactory;
	private Analyzer analyzer;

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

	/**
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	public void afterPropertiesSet() throws Exception {
		if (this.searcherFactory == null)
			throw new BeanInitializationException("directory property required");
		if (this.analyzer == null)
			throw new BeanInitializationException("analyzer property required");        
		this.template=new LuceneSearchTemplate(searcherFactory,analyzer);
	}

	/**
	 * @return
	 */
	public LuceneSearchTemplate getTemplate() {
		return template;
	}

	/**
	 * @param template
	 */
	public void setTemplate(LuceneSearchTemplate template) {
		this.template = template;
	}

	/**
	 * @return
	 */
	public SearcherFactory getSearcherFactory() {
		return searcherFactory;
	}

	/**
	 * @param factory
	 */
	public void setSearcherFactory(SearcherFactory factory) {
		searcherFactory = factory;
	}

}
