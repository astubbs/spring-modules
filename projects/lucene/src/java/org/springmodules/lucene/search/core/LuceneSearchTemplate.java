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

import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.HitCollector;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Searcher;
import org.apache.lucene.search.Sort;

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
 * @see org.springmodules.lucene.search.query.QueryCreator
 * @see org.springmodules.lucene.search.factory
 */
public interface LuceneSearchTemplate {

	/**
	 * Search the index basing a Lucene query created thanks to a callback
	 * method defined in the QueryCreator interface. In this case, the
	 * exceptions during the query creation are managed by the template.
	 * @param queryConstructor the query constructor
	 * @param extractor the extractor of hit informations
	 * @return the search results
	 * @see QueryCreator#createQuery(Analyzer)
	 */
	List search(QueryCreator queryCreator, HitExtractor extractor);

	List search(QueryCreator queryCreator, HitExtractor extractor, QueryResultCreator resultCreator);

	/**
	 * Search the index basing a Lucene query created outside the template.
	 * In this case, the application needs to manage exceptions.
	 * @param queryConstructor the query constructor
	 * @param extractor the extractor of hit informations
	 * @return the search results
	 */
	List search(Query query, HitExtractor extractor);

	List search(Query query, HitExtractor extractor, QueryResultCreator resultCreator);

	/**
	 * Search the index basing a Lucene query created thanks to a callback
	 * method defined in the QueryCreator interface and using a Lucene
	 * filter. In this case, the exceptions during the query creation are
	 * managed by the template.
	 * @param queryConstructor the query constructor
	 * @param extractor the extractor of hit informations
	 * @return the search results
	 * @see QueryCreator#createQuery(Analyzer)
	 */
	List search(QueryCreator queryCreator, HitExtractor extractor, Filter filter);

	List search(QueryCreator queryCreator, HitExtractor extractor,
						QueryResultCreator result, Filter filter);

	/**
	 * Search the index basing a Lucene query created outside the template using
	 * a Lucene filter. In this case, the application needs to manage exceptions.
	 * @param queryConstructor the query constructor
	 * @param extractor the extractor of hit informations
	 * @return the search results
	 */
	List search(Query query, HitExtractor extractor, Filter filter);

	List search(Query query, HitExtractor extractor,
					QueryResultCreator resultCreator, Filter filter);

	/**
	 * Search the index basing a Lucene query created thanks to a callback
	 * method defined in the QueryCreator interface and using a Lucene
	 * sort. In this case, the exceptions during the query creation are
	 * managed by the template.
	 * @param queryConstructor the query constructor
	 * @param extractor the extractor of hit informations
	 * @return the search results
	 * @see QueryCreator#createQuery(Analyzer)
	 */
	List search(QueryCreator queryCreator, HitExtractor extractor, Sort sort);

	List search(QueryCreator queryCreator, HitExtractor extractor,
								QueryResultCreator resultCreator, Sort sort);

	/**
	 * Search the index basing a Lucene query created outside the template using
	 * a Lucene sort. In this case, the application needs to manage exceptions.
	 * @param queryConstructor the query constructor
	 * @param extractor the extractor of hit informations
	 * @return the search results
	 */
	List search(Query query, HitExtractor extractor, Sort sort);

	List search(Query query, HitExtractor extractor, QueryResultCreator resultCreator, Sort sort);
	/**
	 * Search the index basing a Lucene query created thanks to a callback
	 * method defined in the QueryCreator interface and using a Lucene
	 * filter and sort. In this case, the exceptions during the query creation are
	 * managed by the template.
	 * @param queryConstructor the query constructor
	 * @param extractor the extractor of hit informations
	 * @return the search results
	 * @see QueryCreator#createQuery(Analyzer)
	 */
	List search(QueryCreator queryCreator, HitExtractor extractor, Filter filter, Sort sort);

	List search(QueryCreator queryCreator, HitExtractor extractor,
				QueryResultCreator resultCreator, Filter filter, Sort sort);

	/**
	 * Search the index basing a Lucene query created outside the template using
	 * a Lucene filter and sort. In this case, the application needs to manage
	 * exceptions.
	 * @param queryConstructor the query constructor
	 * @param extractor the extractor of hit informations
	 * @return the search results
	 */
	List search(Query query, HitExtractor extractor, Filter filter, Sort sort);

	List search(Query query, HitExtractor extractor,
					QueryResultCreator resultCreator, Filter filter, Sort sort);

	/**
	 * Search the index basing a Lucene query created thanks to a callback
	 * method defined in the QueryCreator interface. In this case, the
	 * exceptions during the query creation are managed by the template.
	 * The results are collecting with the Lucene HitCollector parameter.
	 * @param queryConstructor the query constructor
	 * @param results the Lucene hit collector
	 * @see QueryCreator#createQuery(Analyzer)
	 * @see org.apache.lucene.search.HitCollector
	 */
	void search(QueryCreator queryCreator, HitCollector results);

	/**
	 * Execute the action specified by the given action object within a
	 * Lucene Searcher.
	 * Note: if you share resources across several calls, the Searcher
	 * provides to the callback is the shared instance. A new one is not
	 * created.
	 * @param callback the callback object that exposes the Searcher
	 * @see SearcherCallback#doWithSearcher(Searcher)
	 */
	Object search(SearcherCallback callback);

}
