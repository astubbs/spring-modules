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

package org.springmodules.lucene.search.core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.Searcher;
import org.apache.lucene.search.Sort;
import org.springmodules.lucene.search.LuceneSearchException;
import org.springmodules.lucene.search.factory.SearcherFactory;
import org.springmodules.lucene.search.factory.SearcherFactoryUtils;
import org.springmodules.lucene.search.query.QueryCreator;

/**
 * Template class to make search using Lucene.
 * 
 * @author Brian McCallister
 * @author Thierry Templier
 */
public class LuceneSearchTemplate {

	private SearcherFactory searcherFactory;
	private Analyzer analyzer;

	/**
	 * Construct a new LuceneSearchTemplate for bean usage.
	 * Note: The SearcherFactory has to be set before using the instance.
	 * This constructor can be used to prepare a LuceneSearchTemplate via a BeanFactory,
	 * typically setting the SearcherFactory via setSearcherFactory.
	 * @see #setSearcherFactory
	 */
	public LuceneSearchTemplate() {
	}

	/**
	 * @param searcherFactory
	 * @param analyzer
	 */
	public LuceneSearchTemplate(SearcherFactory searcherFactory,Analyzer analyzer) {
		setSearcherFactory(searcherFactory);
		setAnalyzer(analyzer);
		afterPropertiesSet();
	}

	/**
	 * Eagerly initialize the exception translator,
	 * creating a default one for the specified SearcherFactory if none set.
	 */
	public void afterPropertiesSet() {
		if (getSearcherFactory() == null) {
			throw new IllegalArgumentException("searcherFactory is required");
		}
	}

	/**
	 * This method is used to extract datas from the hists.
	 * 
	 * @param hits the hists corresponding to the search
	 * @param extractor the extractor used
	 * @return the search results
	 * @throws IOException
	 */
	protected List extractHits(Hits hits,HitExtractor extractor) throws IOException {
		List list=new ArrayList();
		for(int cpt=0;cpt<hits.length();cpt++) {
			list.add(extractor.mapHit(hits.id(cpt),hits.doc(cpt),hits.score(cpt)));
		}
		return list;
	}

	/**
	 * This method is used to execute a search based on a
	 * specified query and extract the datas included in the
	 * hits.
	 * 
	 * @param queryConstructor the query constructor
	 * @param extractor the extractor of hit informations
	 * @return the search results
	 */
	public List search(QueryCreator queryConstructor,HitExtractor extractor) {
		return doSearch(queryConstructor,extractor,null,null);
	}

	public List search(QueryCreator queryConstructor,HitExtractor extractor,Filter filter) {
		return doSearch(queryConstructor,extractor,filter,null);
	}

	public List search(QueryCreator queryConstructor,HitExtractor extractor,Sort sort) {
		return doSearch(queryConstructor,extractor,null,sort);
	}

	public List search(QueryCreator queryConstructor,HitExtractor extractor,Filter filter,Sort sort) {
		return doSearch(queryConstructor,extractor,filter,sort);
	}

	private List doSearch(QueryCreator queryConstructor,HitExtractor extractor,Filter filter,Sort sort) {
		Searcher searcher=SearcherFactoryUtils.getSearcher(getSearcherFactory());
		try {
			Hits hits=null;
			if( filter!=null && sort!=null ) {
				hits=searcher.search(queryConstructor.createQuery(getAnalyzer()),filter,sort);
			} else if( filter!=null ) { 
				hits=searcher.search(queryConstructor.createQuery(getAnalyzer()),filter);
			} else if( sort!=null ) { 
				hits=searcher.search(queryConstructor.createQuery(getAnalyzer()),sort);
			} else { 
				hits=searcher.search(queryConstructor.createQuery(getAnalyzer()));
			}
			return extractHits(hits,extractor);
		} catch (IOException ex) {
			throw new LuceneSearchException("Error during the search",ex);
		} catch (ParseException ex) {
			throw new LuceneSearchException("Error during the parse of the query",ex);
		} finally {
			SearcherFactoryUtils.closeSearcherIfNecessary(getSearcherFactory(),searcher);
		}
	}

	/**
	 * This method is used to work directly with a Lucene searcher
	 * using a dedicated callback.
	 * 
	 * @param callback the searcher callback
	 * @return the search results
	 */
	public Object search(SearcherCallback callback) {
		Searcher searcher=SearcherFactoryUtils.getSearcher(getSearcherFactory());
		try {
			return callback.doWithSearcher(searcher);
		} catch (IOException ex) {
			throw new LuceneSearchException("Error during searching",ex);
		} catch (ParseException ex) {
			throw new LuceneSearchException("Error during parsing query",ex);
		} finally {
			SearcherFactoryUtils.closeSearcherIfNecessary(getSearcherFactory(),searcher);
		}
	}

	/**
	 * @return
	 */
	public Analyzer getAnalyzer() {
		return analyzer;
	}

	/**
	 * @return
	 */
	public SearcherFactory getSearcherFactory() {
		return searcherFactory;
	}

	/**
	 * @param analyzer
	 */
	public void setAnalyzer(Analyzer analyzer) {
		this.analyzer = analyzer;
	}

	/**
	 * @param factory
	 */
	public void setSearcherFactory(SearcherFactory factory) {
		searcherFactory = factory;
	}

}
