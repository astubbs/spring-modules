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
import org.springmodules.lucene.search.core.DefaultLuceneSearchTemplate;
import org.springmodules.lucene.search.core.LuceneSearchTemplate;
import org.springmodules.lucene.search.factory.SearcherFactory;

/**
 * Convenient super class for objects using Lucene search
 * using a LuceneSearchTemplate instance.
 * Requires a SearcherFactory and an Analyzer to be set if the
 * template is not injected directly and providing a
 * LuceneSearchTemplate based on it to subclasses.
 *
 * <p>This base class is mainly intended for LuceneSearchTemplate usage
 * but can also be used when working with SearcherFactoryUtils directly
 * or with indexer classes.
 *
 * @author Thierry Templier
 */
public abstract class LuceneSearchSupport implements InitializingBean {
	private LuceneSearchTemplate template;
	private SearcherFactory searcherFactory;
	private Analyzer analyzer;

	/**
	 * Set the SearcherFactory to obtain a Searcher.
	 */
	public void setSearcherFactory(SearcherFactory factory) {
		searcherFactory = factory;
	}

	/**
	 * Return the SearcherFactory used by this template.
	 */
	public SearcherFactory getSearcherFactory() {
		return searcherFactory;
	}

	/**
	 * Set the default Lucene Analyzer used to construct Lucene queries.
	 */
	public void setAnalyzer(Analyzer analyzer) {
		this.analyzer = analyzer;
	}

	/**
	 * Return the Lucene Analyzer used by this template.
	 */
	public Analyzer getAnalyzer() {
		return analyzer;
	}

	/**
	 * Set a configured template.
	 */
	public void setLuceneSearcherTemplate(LuceneSearchTemplate template) {
		this.template = template;
	}

	/**
	 * Return the LuceneSearchTemplate to use.
	 */
	public LuceneSearchTemplate getLuceneSearcherTemplate() {
		return template;
	}

	/**
	 * This method constructs a LuceneSearchTemplate basing indexFactory
	 * and analyzer properties if it is not injected. 
	 */
	public void afterPropertiesSet() throws Exception {
		if( this.template==null ) {
			if (this.searcherFactory == null)
				throw new BeanInitializationException("directory property required");
			if (this.analyzer == null)
				throw new BeanInitializationException("analyzer property required");        

			this.template = new DefaultLuceneSearchTemplate(searcherFactory, analyzer);
		}
	}

}
