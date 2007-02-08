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
import org.apache.lucene.analysis.SimpleAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.QueryFilter;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.TermQuery;
import org.easymock.MockControl;
import org.springmodules.lucene.AbstractLuceneTestCase;
import org.springmodules.lucene.search.factory.LuceneHits;
import org.springmodules.lucene.search.factory.LuceneSearcher;
import org.springmodules.lucene.search.factory.SearcherFactory;
import org.springmodules.lucene.search.factory.SimpleSearcherFactory;

/**
 * @author Brian McCallister
 * @author Thierry Templier
 */
public class LuceneSearchTemplateTests extends AbstractLuceneTestCase {

	/*
	 * Test for List search(QueryCreator, HitExtractor)
	 */
	final public void testSearchQueryCreatorHitExtractor() throws Exception {
		Analyzer analyzer = new SimpleAnalyzer();
		MockControl searcherFactoryControl = MockControl.createStrictControl(SearcherFactory.class);
		SearcherFactory searcherFactory = (SearcherFactory)searcherFactoryControl.getMock();
		MockControl searcherControl = MockControl.createStrictControl(LuceneSearcher.class);
		LuceneSearcher searcher = (LuceneSearcher)searcherControl.getMock();
		MockControl queryCreatorControl = MockControl.createStrictControl(QueryCreator.class);
		QueryCreator queryCreator = (QueryCreator)queryCreatorControl.getMock();
		MockControl hitsControl = MockControl.createStrictControl(LuceneHits.class);
		LuceneHits hits = (LuceneHits)hitsControl.getMock();
		MockControl hitExtractorControl = MockControl.createStrictControl(HitExtractor.class);
		HitExtractor hitExtractor = (HitExtractor)hitExtractorControl.getMock();

		//query
		Query query = new TermQuery(new Term("field", "sample"));
		
		//document
		Document document = new Document();
		document.add(new Field("field", "a sample", Field.Store.YES, Field.Index.TOKENIZED));
		document.add(new Field("filter", "a sample filter", Field.Store.YES, Field.Index.TOKENIZED));
		document.add(new Field("sort", "2", Field.Store.YES, Field.Index.UN_TOKENIZED));
		Object obj = new Object();

		searcherFactory.getSearcher();
		searcherFactoryControl.setReturnValue(searcher, 1);
		
		queryCreator.createQuery(analyzer);
		System.out.println("query = "+query);
		queryCreatorControl.setReturnValue(query, 1);
		
		searcher.search(query);
		searcherControl.setReturnValue(hits, 1);

		hits.length();
		hitsControl.setReturnValue(1, 1);
		
		hits.id(0);
		hitsControl.setReturnValue(1, 1);
		
		hits.doc(0);
		hitsControl.setReturnValue(document, 1);
		
		hits.score(0);
		hitsControl.setReturnValue((float)1, 1);
		
		hitExtractor.mapHit(1, document, 1);
		hitExtractorControl.setReturnValue(obj, 1);
		
		hits.length();
		hitsControl.setReturnValue(1, 1);
		
		searcher.close();
		searcherControl.setVoidCallable(1);

		searcherFactoryControl.replay();
		searcherControl.replay();
		queryCreatorControl.replay();
		hitExtractorControl.replay();
		hitsControl.replay();
		
		//Lucene template
		LuceneSearchTemplate template = new DefaultLuceneSearchTemplate(searcherFactory, analyzer);
		List results = template.search(queryCreator, hitExtractor);

		searcherFactoryControl.verify();
		searcherControl.verify();
		queryCreatorControl.verify();
		hitExtractorControl.verify();
		hitsControl.verify();
		
		assertEquals(results.size(), 1);
		assertEquals(results.get(0), obj);
		
	}

	/*
	 * Test for List search(QueryCreator, HitExtractor)
	 */
	final public void testSearchParsedQueryCreatorHitExtractor() throws Exception {
		Analyzer analyzer = new SimpleAnalyzer();
		MockControl searcherFactoryControl = MockControl.createStrictControl(SearcherFactory.class);
		SearcherFactory searcherFactory = (SearcherFactory)searcherFactoryControl.getMock();
		MockControl searcherControl = MockControl.createStrictControl(LuceneSearcher.class);
		LuceneSearcher searcher = (LuceneSearcher)searcherControl.getMock();
		MockControl hitsControl = MockControl.createStrictControl(LuceneHits.class);
		LuceneHits hits = (LuceneHits)hitsControl.getMock();
		MockControl hitExtractorControl = MockControl.createStrictControl(HitExtractor.class);
		HitExtractor hitExtractor = (HitExtractor)hitExtractorControl.getMock();

		//query
		final boolean[] called = { false };
		ParsedQueryCreator queryCreator = new ParsedQueryCreator() {
			public QueryParams configureQuery() {
				called[0] = true; 
				return new QueryParams("field","lucene");
			}
		};
		final Query query = queryCreator.createQuery(analyzer);
		
		//document
		Document document = new Document();
		document.add(new Field("field", "a sample", Field.Store.YES, Field.Index.TOKENIZED));
		document.add(new Field("filter", "a sample filter", Field.Store.YES, Field.Index.TOKENIZED));
		document.add(new Field("sort", "2", Field.Store.YES, Field.Index.UN_TOKENIZED));
		Object obj = new Object();

		searcherFactory.getSearcher();
		searcherFactoryControl.setReturnValue(searcher, 1);
		
		searcher.search(query);
		searcherControl.setReturnValue(hits, 1);

		hits.length();
		hitsControl.setReturnValue(1, 1);
		
		hits.id(0);
		hitsControl.setReturnValue(1, 1);
		
		hits.doc(0);
		hitsControl.setReturnValue(document, 1);
		
		hits.score(0);
		hitsControl.setReturnValue((float)1, 1);
	
		hitExtractor.mapHit(1, document, 1);
		hitExtractorControl.setReturnValue(obj, 1);
		
		hits.length();
		hitsControl.setReturnValue(1, 1);
		
		searcher.close();
		searcherControl.setVoidCallable(1);

		searcherFactoryControl.replay();
		searcherControl.replay();
		hitExtractorControl.replay();
		hitsControl.replay();
		
		//Lucene template
		LuceneSearchTemplate template = new DefaultLuceneSearchTemplate(searcherFactory, analyzer);
		List results = template.search(queryCreator, hitExtractor);

		searcherFactoryControl.verify();
		searcherControl.verify();
		hitExtractorControl.verify();
		hitsControl.verify();

		assertTrue(called[0]);
		assertEquals(results.size(), 1);
		assertEquals(results.get(0), obj);
	}

	/*
	 * Test for List search(QueryCreator, HitExtractor, Filter)
	 */
	final public void testSearchQueryCreatorHitExtractorFilter() throws Exception {
		Analyzer analyzer = new SimpleAnalyzer();
		MockControl searcherFactoryControl = MockControl.createStrictControl(SearcherFactory.class);
		SearcherFactory searcherFactory = (SearcherFactory)searcherFactoryControl.getMock();
		MockControl searcherControl = MockControl.createStrictControl(LuceneSearcher.class);
		LuceneSearcher searcher = (LuceneSearcher)searcherControl.getMock();
		MockControl queryCreatorControl = MockControl.createStrictControl(QueryCreator.class);
		QueryCreator queryCreator = (QueryCreator)queryCreatorControl.getMock();
		MockControl hitsControl = MockControl.createStrictControl(LuceneHits.class);
		LuceneHits hits = (LuceneHits)hitsControl.getMock();
		MockControl hitExtractorControl = MockControl.createStrictControl(HitExtractor.class);
		HitExtractor hitExtractor = (HitExtractor)hitExtractorControl.getMock();

		//query
		Query query = new TermQuery(new Term("field", "sample"));
		
		//filter
		TermQuery filterQuery = new TermQuery(new Term("filter", "another"));
		Filter filter = new QueryFilter(filterQuery);
		
		//document
		Document document = new Document();
		document.add(new Field("field", "a sample", Field.Store.YES, Field.Index.TOKENIZED));
		document.add(new Field("filter", "a sample filter", Field.Store.YES, Field.Index.TOKENIZED));
		document.add(new Field("sort", "2", Field.Store.YES, Field.Index.UN_TOKENIZED));
		Object obj = new Object();

		searcherFactory.getSearcher();
		searcherFactoryControl.setReturnValue(searcher, 1);
		
		queryCreator.createQuery(analyzer);
		System.out.println("query = "+query);
		queryCreatorControl.setReturnValue(query, 1);
		
		searcher.search(query, filter);
		searcherControl.setReturnValue(hits, 1);

		hits.length();
		hitsControl.setReturnValue(1, 1);
		
		hits.id(0);
		hitsControl.setReturnValue(1, 1);
		
		hits.doc(0);
		hitsControl.setReturnValue(document, 1);
		
		hits.score(0);
		hitsControl.setReturnValue((float)1, 1);
		
		hitExtractor.mapHit(1, document, 1);
		hitExtractorControl.setReturnValue(obj, 1);
		
		hits.length();
		hitsControl.setReturnValue(1, 1);
		
		searcher.close();
		searcherControl.setVoidCallable(1);

		searcherFactoryControl.replay();
		searcherControl.replay();
		queryCreatorControl.replay();
		hitExtractorControl.replay();
		hitsControl.replay();
		
		//Lucene template
		LuceneSearchTemplate template = new DefaultLuceneSearchTemplate(searcherFactory, analyzer);
		List results = template.search(queryCreator, hitExtractor, filter);

		searcherFactoryControl.verify();
		searcherControl.verify();
		queryCreatorControl.verify();
		hitExtractorControl.verify();
		hitsControl.verify();
		
		assertEquals(results.size(), 1);
		assertEquals(results.get(0), obj);
	}

	/*
	 * Test for List search(QueryCreator, HitExtractor, Sort)
	 */
	final public void testSearchQueryCreatorHitExtractorSort() throws Exception {
		Analyzer analyzer = new SimpleAnalyzer();
		MockControl searcherFactoryControl = MockControl.createStrictControl(SearcherFactory.class);
		SearcherFactory searcherFactory = (SearcherFactory)searcherFactoryControl.getMock();
		MockControl searcherControl = MockControl.createStrictControl(LuceneSearcher.class);
		LuceneSearcher searcher = (LuceneSearcher)searcherControl.getMock();
		MockControl queryCreatorControl = MockControl.createStrictControl(QueryCreator.class);
		QueryCreator queryCreator = (QueryCreator)queryCreatorControl.getMock();
		MockControl hitsControl = MockControl.createStrictControl(LuceneHits.class);
		LuceneHits hits = (LuceneHits)hitsControl.getMock();
		MockControl hitExtractorControl = MockControl.createControl(HitExtractor.class);
		HitExtractor hitExtractor = (HitExtractor)hitExtractorControl.getMock();

		//query
		Query query = new TermQuery(new Term("field", "sample"));
		
		//sort
		Sort sort = new Sort("sort");
		
		//document
		Document document = new Document();
		document.add(new Field("field", "a sample", Field.Store.YES, Field.Index.TOKENIZED));
		document.add(new Field("filter", "a sample filter", Field.Store.YES, Field.Index.TOKENIZED));
		document.add(new Field("sort", "2", Field.Store.YES, Field.Index.UN_TOKENIZED));
		Object obj = new Object();

		searcherFactory.getSearcher();
		searcherFactoryControl.setReturnValue(searcher, 1);
		
		queryCreator.createQuery(analyzer);
		System.out.println("query = "+query);
		queryCreatorControl.setReturnValue(query, 1);
		
		searcher.search(query, sort);
		searcherControl.setReturnValue(hits, 1);

		hits.length();
		hitsControl.setReturnValue(1, 1);
		
		hits.id(0);
		hitsControl.setReturnValue(1, 1);
		
		hits.doc(0);
		hitsControl.setReturnValue(document, 1);
		
		hits.score(0);
		hitsControl.setReturnValue((float)1, 1);
		
		hitExtractor.mapHit(1, document, 1);
		hitExtractorControl.setReturnValue(obj, 1);
		
		hits.length();
		hitsControl.setReturnValue(1, 1);
		
		searcher.close();
		searcherControl.setVoidCallable(1);

		searcherFactoryControl.replay();
		searcherControl.replay();
		queryCreatorControl.replay();
		hitExtractorControl.replay();
		hitsControl.replay();
		
		//Lucene template
		LuceneSearchTemplate template = new DefaultLuceneSearchTemplate(searcherFactory, analyzer);
		List results = template.search(queryCreator, hitExtractor, sort);

		searcherFactoryControl.verify();
		searcherControl.verify();
		queryCreatorControl.verify();
		hitExtractorControl.verify();
		hitsControl.verify();
		
		assertEquals(results.size(), 1);
		assertEquals(results.get(0), obj);
	}

	/*
	 * Test for List search(QueryCreator, HitExtractor, Filter, Sort)
	 */
	final public void testSearchQueryCreatorHitExtractorFilterSort() throws Exception {
		Analyzer analyzer = new SimpleAnalyzer();
		MockControl searcherFactoryControl = MockControl.createStrictControl(SearcherFactory.class);
		SearcherFactory searcherFactory = (SearcherFactory)searcherFactoryControl.getMock();
		MockControl searcherControl = MockControl.createStrictControl(LuceneSearcher.class);
		LuceneSearcher searcher = (LuceneSearcher)searcherControl.getMock();
		MockControl queryCreatorControl = MockControl.createStrictControl(QueryCreator.class);
		QueryCreator queryCreator = (QueryCreator)queryCreatorControl.getMock();
		MockControl hitsControl = MockControl.createStrictControl(LuceneHits.class);
		LuceneHits hits = (LuceneHits)hitsControl.getMock();
		MockControl hitExtractorControl = MockControl.createStrictControl(HitExtractor.class);
		HitExtractor hitExtractor = (HitExtractor)hitExtractorControl.getMock();

		//query
		Query query = new TermQuery(new Term("field", "sample"));
		
		//filter
		TermQuery filterQuery = new TermQuery(new Term("filter", "another"));
		Filter filter = new QueryFilter(filterQuery);

		//sort
		Sort sort = new Sort("sort");
		
		//document
		Document document = new Document();
		document.add(new Field("field", "a sample", Field.Store.YES, Field.Index.TOKENIZED));
		document.add(new Field("filter", "a sample filter", Field.Store.YES, Field.Index.TOKENIZED));
		document.add(new Field("sort", "2", Field.Store.YES, Field.Index.UN_TOKENIZED));
		Object obj = new Object();

		searcherFactory.getSearcher();
		searcherFactoryControl.setReturnValue(searcher, 1);
		
		queryCreator.createQuery(analyzer);
		System.out.println("query = "+query);
		queryCreatorControl.setReturnValue(query, 1);
		
		searcher.search(query, filter, sort);
		searcherControl.setReturnValue(hits, 1);

		hits.length();
		hitsControl.setReturnValue(1, 1);
		
		hits.id(0);
		hitsControl.setReturnValue(1, 1);
		
		hits.doc(0);
		hitsControl.setReturnValue(document, 1);
		
		hits.score(0);
		hitsControl.setReturnValue((float)1, 1);
		
		hitExtractor.mapHit(1, document, 1);
		hitExtractorControl.setReturnValue(obj, 1);
		
		hits.length();
		hitsControl.setReturnValue(1, 1);
		
		searcher.close();
		searcherControl.setVoidCallable(1);

		searcherFactoryControl.replay();
		searcherControl.replay();
		queryCreatorControl.replay();
		hitExtractorControl.replay();
		hitsControl.replay();
		
		//Lucene template
		LuceneSearchTemplate template = new DefaultLuceneSearchTemplate(searcherFactory, analyzer);
		List results = template.search(queryCreator, hitExtractor, filter, sort);

		searcherFactoryControl.verify();
		searcherControl.verify();
		queryCreatorControl.verify();
		hitExtractorControl.verify();
		hitsControl.verify();
		
		assertEquals(results.size(), 1);
		assertEquals(results.get(0), obj);
	}

	/*
	 * Test for Object search(SearcherCallback)
	 */
	final public void testSearchSearcherCallback() throws Exception {
		MockControl searcherFactoryControl = MockControl.createStrictControl(SearcherFactory.class);
		SearcherFactory searcherFactory = (SearcherFactory)searcherFactoryControl.getMock();
		MockControl searcherControl = MockControl.createStrictControl(LuceneSearcher.class);
		LuceneSearcher searcher = (LuceneSearcher)searcherControl.getMock();
		MockControl searcherCallbackControl = MockControl.createStrictControl(SearcherCallback.class);
		SearcherCallback searcherCallback = (SearcherCallback)searcherCallbackControl.getMock();

		searcherFactory.getSearcher();
		searcherFactoryControl.setReturnValue(searcher, 1);
		
		searcherCallback.doWithSearcher(searcher);
		searcherCallbackControl.setReturnValue("return", 1);
		
		searcher.close();
		searcherControl.setVoidCallable(1);
		
		searcherControl.replay();
		searcherFactoryControl.replay();
		searcherCallbackControl.replay();
		
		//Lucene template
		LuceneSearchTemplate template = new DefaultLuceneSearchTemplate(searcherFactory,new SimpleAnalyzer());
		String ret = (String)template.search(searcherCallback);

		searcherControl.verify();
		searcherFactoryControl.verify();
		searcherCallbackControl.verify();
		
		assertEquals(ret, "return");
	}

}
