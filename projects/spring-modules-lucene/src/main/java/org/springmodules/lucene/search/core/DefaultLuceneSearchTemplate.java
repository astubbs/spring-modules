/*
 * Copyright 2002-2007 the original author or authors.
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
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.HitCollector;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Sort;
import org.springmodules.lucene.search.LuceneSearchException;
import org.springmodules.lucene.search.factory.LuceneHits;
import org.springmodules.lucene.search.factory.LuceneSearcher;
import org.springmodules.lucene.search.factory.SearcherFactory;
import org.springmodules.lucene.search.factory.SearcherFactoryUtils;

/**
 * <b>This is the central class in the lucene search core package.</b>
 * It simplifies the use of lucene to search documents using searcher.
 * It helps to avoid common errors and to manage these resource in a
 * flexible manner.
 * It executes core Lucene workflow, leaving application code to focus on
 * the way to create Lucene queries and extract data from results.
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
 * @author Alex Burgel
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

	/**
	 * Build the default HitsExtractor. Used when an HitsExtractor is not provided.
	 * @return the default HitsExtractor 
	 */
	protected QueryResultCreator createDefaultQueryResultCreator() {
		return new QueryResultCreator() {
			public List createResult(LuceneHits hits, HitExtractor hitExtractor) throws IOException {
				List result = new ArrayList();
				for(int cpt=0; cpt<hits.length(); cpt++) {
					result.add(hitExtractor.mapHit(hits.id(cpt), hits.doc(cpt), hits.score(cpt)));
				}
				return result;
			}
		};
	}
	
	public List search(QueryCreator queryCreator, HitExtractor extractor) {
		return doSearch(createQuery(queryCreator), extractor,
				createDefaultQueryResultCreator(), null, null);
	}

	public List search(QueryCreator queryCreator, HitExtractor extractor, QueryResultCreator resultCreator) {
		return doSearch(createQuery(queryCreator), extractor, resultCreator, null, null);
	}

	public List search(Query query, HitExtractor extractor) {
		return doSearch(query, extractor, createDefaultQueryResultCreator(), null, null);
	}

	public List search(Query query, HitExtractor extractor, QueryResultCreator resultCreator) {
		return doSearch(query, extractor, resultCreator, null, null);
	}

	public List search(QueryCreator queryCreator, HitExtractor extractor, Filter filter) {
		return doSearch(createQuery(queryCreator), extractor,
				createDefaultQueryResultCreator(), filter, null);
	}

	public List search(QueryCreator queryCreator, HitExtractor extractor,
						QueryResultCreator resultCreator, Filter filter) {
		return doSearch(createQuery(queryCreator), extractor,
									resultCreator, filter, null);
	}

	public List search(Query query, HitExtractor extractor, Filter filter) {
		return doSearch(query, extractor,
				createDefaultQueryResultCreator(), filter, null);
	}

	public List search(Query query, HitExtractor extractor,
						QueryResultCreator resultCreator, Filter filter) {
		return doSearch(query, extractor, resultCreator, filter, null);
	}

	public List search(QueryCreator queryCreator, HitExtractor extractor, Sort sort) {
		return doSearch(createQuery(queryCreator), extractor,
				createDefaultQueryResultCreator(), null, sort);
	}

	public List search(QueryCreator queryCreator, HitExtractor extractor,
										QueryResultCreator resultCreator, Sort sort) {
		return doSearch(createQuery(queryCreator), extractor, resultCreator, null, sort);
	}

	public List search(Query query, HitExtractor extractor, Sort sort) {
		return doSearch(query, extractor,
				createDefaultQueryResultCreator(), null, sort);
	}

	public List search(Query query, HitExtractor extractor,
						QueryResultCreator resultCreator, Sort sort) {
		return doSearch(query, extractor, resultCreator, null, sort);
	}

	public List search(QueryCreator queryCreator, HitExtractor extractor, Filter filter, Sort sort) {
		return doSearch(createQuery(queryCreator), extractor,
				createDefaultQueryResultCreator(), filter, sort);
	}

	public List search(QueryCreator queryCreator, HitExtractor extractor,
						QueryResultCreator resultCreator, Filter filter, Sort sort) {
		return doSearch(createQuery(queryCreator), extractor, resultCreator, filter, sort);
	}

	public List search(Query query, HitExtractor extractor, Filter filter, Sort sort) {
		return doSearch(query, extractor,
				createDefaultQueryResultCreator(), filter, sort);
	}

	public List search(Query query, HitExtractor extractor,
						QueryResultCreator resultCreator, Filter filter, Sort sort) {
		return doSearch(query, extractor, resultCreator, filter, sort);
	}

	/**
	 * Internal method to search the index basing a Lucene query created
	 * thanks to a callback method defined in the QueryCreator interface.
	 * In this case, the exceptions during the query creation are managed
	 * by the template.
	 * This method uses sort and/or filter parameters as Searcher search
	 * method parameters if they are not null. 
	 * @param query the query used
	 * @param extractor the extractor of hit informations
	 * @param filter the query filter
	 * @param sort the query sorter
	 * @return the search results
	 */
	private List doSearch(Query query, HitExtractor hitExtractor,
					QueryResultCreator queryResultCreator, Filter filter, Sort sort) {
		LuceneSearcher searcher = SearcherFactoryUtils.getSearcher(getSearcherFactory());
		try {
			LuceneHits hits = null;
			if( filter!=null && sort!=null ) {
				hits = searcher.search(query, filter, sort);
			} else if( filter!=null ) { 
				hits = searcher.search(query, filter);
			} else if( sort!=null ) { 
				hits = searcher.search(query, sort);
			} else { 
				hits = searcher.search(query);
			}
			//return extractHits(hits, extractor);
			return queryResultCreator.createResult(hits, hitExtractor);
		} catch (IOException ex) {
			throw new LuceneSearchException("Error during the search", ex);
		} finally {
			SearcherFactoryUtils.releaseSearcher(getSearcherFactory(), searcher);
		}
	}

	public void search(QueryCreator queryCreator, HitCollector results) {
		LuceneSearcher searcher = SearcherFactoryUtils.getSearcher(getSearcherFactory());
		try {
			searcher.search(queryCreator.createQuery(getAnalyzer()), results);
		} catch (IOException ex) {
			throw new LuceneSearchException("Error during the search", ex);
		} catch (ParseException ex) {
			throw new LuceneSearchException("Error during the parse of the query", ex);
		} finally {
			SearcherFactoryUtils.releaseSearcher(getSearcherFactory(), searcher);
		}
	}

	public Object search(SearcherCallback callback) {
		LuceneSearcher searcher = SearcherFactoryUtils.getSearcher(getSearcherFactory());
		try {
			return callback.doWithSearcher(searcher);
		} catch (ParseException ex) {
			throw new LuceneSearchException("Error during parsing query", ex);
		} catch (Exception ex) {
			throw new LuceneSearchException("Error during searching", ex);
		} finally {
			SearcherFactoryUtils.releaseSearcher(getSearcherFactory(),searcher);
		}
	}

}
