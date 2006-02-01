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
import org.apache.lucene.search.HitCollector;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Searcher;
import org.apache.lucene.search.Sort;
import org.springmodules.lucene.search.LuceneSearchException;
import org.springmodules.lucene.search.factory.SearcherFactory;
import org.springmodules.lucene.search.factory.SearcherFactoryUtils;

/**
 * <b>This is the central class in the lucene search core package.</b>
 * It simplifies the use of lucene to search documents using searcher.
 * It helps to avoid common errors and to manage these resource in a
 * flexible manner.
 * It executes core Lucene workflow, leaving application code to focus on
 * the way to create Lucene queries and extract datas from results.
 *
 * <p>This class is based on the SearcherFactory abstraction which is a
 * factory to create Searcher for a or several configured Directories.
 * They can be local or remote. So the template doesn't need to always
 * hold resources and you can apply different strategies for managing
 * index resources.
 *
 * <p>Can be used within a service implementation via direct instantiation
 * with a SearcherFactory reference, or get prepared in an application context
 * and given to services as bean reference. Note: The SearcherFactory should
 * always be configured as a bean in the application context, in the first case
 * given to the service directly, in the second case to the prepared template.
 * 
 * @author Brian McCallister
 * @author Thierry Templier
 * @see org.springmodules.lucene.search.query.QueryCreator
 * @see org.springmodules.lucene.search.factory
 */
public class DefaultLuceneSearchTemplate implements LuceneSearchTemplate {

	private SearcherFactory searcherFactory;
	private Analyzer analyzer;

	/**
	 * Construct a new LuceneSearchTemplate for bean usage.
	 * Note: The SearcherFactory has to be set before using the instance.
	 * This constructor can be used to prepare a LuceneSearchTemplate via a BeanFactory,
	 * typically setting the SearcherFactory via setSearcherFactory and the Analyzer
	 * via setAnalyzer.
	 * @see #setSearcherFactory
	 * @see #setAnalyzer(Analyzer)
	 */
	public DefaultLuceneSearchTemplate() {
	}

	/**
	 * Construct a new LuceneSearchTemplate, given an SearcherFactory and an
	 * Analyzer to obtain a Searcher and an Analyzer to be used by the queries.
	 * @param searcherFactory SearcherFactory to obtain Searcher
	 * @param analyzer Lucene analyzer used by the queries
	 */
	public DefaultLuceneSearchTemplate(SearcherFactory searcherFactory,Analyzer analyzer) {
		setSearcherFactory(searcherFactory);
		setAnalyzer(analyzer);
		afterPropertiesSet();
	}

	/**
	 * Check if the searcherFactory is set. The analyzer could be not set.
	 */
	public void afterPropertiesSet() {
		if (getSearcherFactory() == null) {
			throw new IllegalArgumentException("searcherFactory is required");
		}
	}

	/**
	 * Set the SearcherFactory to obtain Searcher.
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
	 * Set the default Lucene Analyzer used by the queries.
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
	 * Method used to extract datas from hits. These datas are
	 * the Lucene internal document identifier, the document
	 * itself and the score.
	 * 
	 * @param hits the hits corresponding to the search
	 * @param extractor the extractor specified in the search method
	 * @return the search results extracted
	 * @throws IOException exception occuring when accessing documents
	 */
	private List extractHits(Hits hits,HitExtractor extractor) throws IOException {
		List list=new ArrayList();
		for(int cpt=0;cpt<hits.length();cpt++) {
			list.add(extractor.mapHit(hits.id(cpt),hits.doc(cpt),hits.score(cpt)));
		}
		return list;
	}

	/**
	 * Invoke the given QueryCreator, constructing Lucene query.
	 * @param queryCreator the QueryCreator to invoke
	 * @return the constructed Query
	 * @see QueryCreator#createQuery(Analyzer)
	 */
	protected Query createQuery(QueryCreator queryCreator) {
		try {
			return queryCreator.createQuery(getAnalyzer());
		} catch (ParseException ex) {
			throw new LuceneSearchException("Construction of the desired Query failed", ex);
		}
	}

	public List search(QueryCreator queryCreator,HitExtractor extractor) {
		return doSearch(createQuery(queryCreator),extractor,null,null);
	}

	public List search(Query query,HitExtractor extractor) {
		return doSearch(query,extractor,null,null);
	}

	public List search(QueryCreator queryCreator,HitExtractor extractor,Filter filter) {
		return doSearch(createQuery(queryCreator),extractor,filter,null);
	}

	public List search(Query query,HitExtractor extractor,Filter filter) {
		return doSearch(query,extractor,filter,null);
	}

	public List search(QueryCreator queryCreator,HitExtractor extractor,Sort sort) {
		return doSearch(createQuery(queryCreator),extractor,null,sort);
	}

	public List search(Query query,HitExtractor extractor,Sort sort) {
		return doSearch(query,extractor,null,sort);
	}

	public List search(QueryCreator queryCreator,HitExtractor extractor,Filter filter,Sort sort) {
		return doSearch(createQuery(queryCreator),extractor,filter,sort);
	}

	public List search(Query query,HitExtractor extractor,Filter filter,Sort sort) {
		return doSearch(query,extractor,filter,sort);
	}

	/**
	 * Internal method to search the index basing a Lucene query created
	 * thanks to a callback method defined in the QueryCreator interface.
	 * In this case, the exceptions during the query creation are managed
	 * by the template.
	 * This method uses sort and/or filter paramaters as Searcher search
	 * method parameters if they are not null. 
	 * @param query the query used
	 * @param extractor the extractor of hit informations
	 * @param filter the query filter
	 * @param sort the query sorter
	 * @return the search results
	 */
	private List doSearch(Query query,HitExtractor extractor,Filter filter,Sort sort) {
		Searcher searcher=SearcherFactoryUtils.getSearcher(getSearcherFactory());
		try {
			Hits hits=null;
			if( filter!=null && sort!=null ) {
				hits=searcher.search(query,filter,sort);
			} else if( filter!=null ) { 
				hits=searcher.search(query,filter);
			} else if( sort!=null ) { 
				hits=searcher.search(query,sort);
			} else { 
				hits=searcher.search(query);
			}
			return extractHits(hits,extractor);
		} catch (IOException ex) {
			throw new LuceneSearchException("Error during the search",ex);
		} finally {
			SearcherFactoryUtils.releaseSearcher(getSearcherFactory(),searcher);
		}
	}

	public void search(QueryCreator queryCreator,HitCollector results) {
		Searcher searcher=SearcherFactoryUtils.getSearcher(getSearcherFactory());
		try {
			searcher.search(queryCreator.createQuery(getAnalyzer()),results);
		} catch (IOException ex) {
			throw new LuceneSearchException("Error during the search",ex);
		} catch (ParseException ex) {
			throw new LuceneSearchException("Error during the parse of the query",ex);
		} finally {
			SearcherFactoryUtils.releaseSearcher(getSearcherFactory(),searcher);
		}
	}

	public Object search(SearcherCallback callback) {
		Searcher searcher=SearcherFactoryUtils.getSearcher(getSearcherFactory());
		try {
			return callback.doWithSearcher(searcher);
		} catch (IOException ex) {
			throw new LuceneSearchException("Error during searching",ex);
		} catch (ParseException ex) {
			throw new LuceneSearchException("Error during parsing query",ex);
		} finally {
			SearcherFactoryUtils.releaseSearcher(getSearcherFactory(),searcher);
		}
	}

}
