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
import java.util.List;

import junit.framework.TestCase;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.SimpleAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.QueryFilter;
import org.apache.lucene.search.Searcher;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.store.RAMDirectory;
import org.springmodules.lucene.search.factory.SimpleSearcherFactory;

/**
 * @author Brian McCallister
 * @author Thierry Templier
 */
public class LuceneSearchTemplateTests extends TestCase {

	private RAMDirectory directory;

	/**
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		//Initialization of the index
		this.directory=new RAMDirectory();
		IndexWriter writer=new IndexWriter(directory,new SimpleAnalyzer(),true);
		//Adding a document
		Document document1=new Document();
		document1.add(Field.Text("field", "a sample"));
		document1.add(Field.Text("filter", "a sample filter"));
		document1.add(Field.Keyword("sort", "2"));
		writer.addDocument(document1);
		//Adding a document
		Document document2=new Document();
		document2.add(Field.Text("field", "a Lucene support sample"));
		document2.add(Field.Text("filter", "another sample filter"));
		document2.add(Field.Keyword("sort", "3"));
		writer.addDocument(document2);
		//Adding a document
		Document document3=new Document();
		document3.add(Field.Text("field", "a different sample"));
		document3.add(Field.Text("filter", "another sample filter"));
		document3.add(Field.Keyword("sort", "1"));
		writer.addDocument(document3);
		writer.optimize();
		writer.close();
	}

	/**
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		this.directory=null;
	}

	/*
	 * Test for List search(QueryCreator, HitExtractor)
	 */
	final public void testSearchQueryCreatorHitExtractor() throws Exception {
		//Initialization of the searcher
		SimpleSearcherFactory targetSearcherFactory=new SimpleSearcherFactory(directory);
		MockSimpleSearcherFactory searcherFactory=new MockSimpleSearcherFactory(targetSearcherFactory);

		//Lucene template
		LuceneSearchTemplate template=new LuceneSearchTemplate(searcherFactory,new SimpleAnalyzer());

		//First search
		List results=template.search(new QueryCreator() {
			public Query createQuery(Analyzer analyzer) throws ParseException {
				return new TermQuery(new Term("field","lucene"));
			}
		},new HitExtractor() {
			public Object mapHit(int id, Document document, float score) {
				return document.get("field");
			}
		});

		assertEquals(searcherFactory.getListener().getNumberSearchersCreated(),1);
		assertEquals(searcherFactory.getListener().getNumberSearchersClosed(),1);
		assertEquals(results.size(),1);
		assertEquals((String)results.get(0),"a Lucene support sample");

		//Second search
		results=template.search(new QueryCreator() {
			public Query createQuery(Analyzer analyzer) throws ParseException {
				return new TermQuery(new Term("field","sample"));
			}
		},new HitExtractor() {
			public Object mapHit(int id, Document document, float score) {
				return document.get("field");
			}
		});

		assertEquals(searcherFactory.getListener().getNumberSearchersCreated(),2);
		assertEquals(searcherFactory.getListener().getNumberSearchersClosed(),2);
		assertEquals(results.size(),3);
		assertEquals((String)results.get(0),"a sample");
		assertEquals((String)results.get(1),"a Lucene support sample");
		assertEquals((String)results.get(2),"a different sample");
	}

	/*
	 * Test for List search(QueryCreator, HitExtractor)
	 */
	final public void testSearchParsedQueryCreatorHitExtractor() throws Exception {
		//Initialization of the searcher
		SimpleSearcherFactory targetSearcherFactory=new SimpleSearcherFactory(directory);
		MockSimpleSearcherFactory searcherFactory=new MockSimpleSearcherFactory(targetSearcherFactory);

		//Lucene template
		LuceneSearchTemplate template=new LuceneSearchTemplate(searcherFactory,new SimpleAnalyzer());

		//First search
		List results=template.search(new ParsedQueryCreator() {
			public QueryParams configureQuery() {
				return new QueryParams("field","lucene");
			}
		},new HitExtractor() {
			public Object mapHit(int id, Document document, float score) {
				return document.get("field");
			}
		});

		assertEquals(searcherFactory.getListener().getNumberSearchersCreated(),1);
		assertEquals(searcherFactory.getListener().getNumberSearchersClosed(),1);
		assertEquals(results.size(),1);
		assertEquals((String)results.get(0),"a Lucene support sample");

		//Second search
		results=template.search(new ParsedQueryCreator() {
			public QueryParams configureQuery() {
				return new QueryParams("field","sample");
			}
		},new HitExtractor() {
			public Object mapHit(int id, Document document, float score) {
				return document.get("field");
			}
		});

		assertEquals(searcherFactory.getListener().getNumberSearchersCreated(),2);
		assertEquals(searcherFactory.getListener().getNumberSearchersClosed(),2);
		assertEquals(results.size(),3);
		assertEquals((String)results.get(0),"a sample");
		assertEquals((String)results.get(1),"a Lucene support sample");
		assertEquals((String)results.get(2),"a different sample");
	}

	/*
	 * Test for List search(QueryCreator, HitExtractor, Filter)
	 */
	final public void testSearchQueryCreatorHitExtractorFilter() {
		TermQuery filterQuery = new TermQuery(new Term("filter", "another"));
		Filter filter = new QueryFilter(filterQuery);

		//Initialization of the searcher
		SimpleSearcherFactory targetSearcherFactory=new SimpleSearcherFactory(directory);
		MockSimpleSearcherFactory searcherFactory=new MockSimpleSearcherFactory(targetSearcherFactory);

		//Lucene template
		LuceneSearchTemplate template=new LuceneSearchTemplate(searcherFactory,new SimpleAnalyzer());

		//Query
		List results=template.search(new QueryCreator() {
			public Query createQuery(Analyzer analyzer) throws ParseException {
				return new TermQuery(new Term("field","sample"));
			}
		},new HitExtractor() {
			public Object mapHit(int id, Document document, float score) {
				return document.get("field");
			}
		},filter);

		assertEquals(searcherFactory.getListener().getNumberSearchersCreated(),1);
		assertEquals(results.size(),2);
		assertEquals((String)results.get(0),"a Lucene support sample");
		assertEquals((String)results.get(1),"a different sample");
		assertEquals(searcherFactory.getListener().getNumberSearchersClosed(),1);
	}

	/*
	 * Test for List search(QueryCreator, HitExtractor, Sort)
	 */
	final public void testSearchQueryCreatorHitExtractorSort() {
		Sort sort=new Sort("sort");

		//Initialization of the searcher
		SimpleSearcherFactory targetSearcherFactory=new SimpleSearcherFactory(directory);
		MockSimpleSearcherFactory searcherFactory=new MockSimpleSearcherFactory(targetSearcherFactory);

		//Lucene template
		LuceneSearchTemplate template=new LuceneSearchTemplate(searcherFactory,new SimpleAnalyzer());

		//Query
		List results=template.search(new QueryCreator() {
			public Query createQuery(Analyzer analyzer) throws ParseException {
				return new TermQuery(new Term("field","sample"));
			}
		},new HitExtractor() {
			public Object mapHit(int id, Document document, float score) {
				return document.get("field");
			}
		},sort);

		assertEquals(searcherFactory.getListener().getNumberSearchersCreated(),1);
		assertEquals(results.size(),3);
		assertEquals((String)results.get(0),"a different sample");
		assertEquals((String)results.get(1),"a sample");
		assertEquals((String)results.get(2),"a Lucene support sample");
		assertEquals(searcherFactory.getListener().getNumberSearchersClosed(),1);
	}

	/*
	 * Test for List search(QueryCreator, HitExtractor, Filter, Sort)
	 */
	final public void testSearchQueryCreatorHitExtractorFilterSort() {
		TermQuery filterQuery = new TermQuery(new Term("filter", "another"));
		Filter filter = new QueryFilter(filterQuery);
		Sort sort=new Sort("sort");

		//Initialization of the searcher
		SimpleSearcherFactory targetSearcherFactory=new SimpleSearcherFactory(directory);
		MockSimpleSearcherFactory searcherFactory=new MockSimpleSearcherFactory(targetSearcherFactory);

		//Lucene template
		LuceneSearchTemplate template=new LuceneSearchTemplate(searcherFactory,new SimpleAnalyzer());

		//Query
		List results=template.search(new QueryCreator() {
			public Query createQuery(Analyzer analyzer) throws ParseException {
				return new TermQuery(new Term("field","sample"));
			}
		},new HitExtractor() {
			public Object mapHit(int id, Document document, float score) {
				return document.get("field");
			}
		},filter,sort);

		assertEquals(searcherFactory.getListener().getNumberSearchersCreated(),1);
		assertEquals(results.size(),2);
		assertEquals((String)results.get(0),"a different sample");
		assertEquals((String)results.get(1),"a Lucene support sample");
		assertEquals(searcherFactory.getListener().getNumberSearchersClosed(),1);
	}

	/*
	 * Test for Object search(SearcherCallback)
	 */
	final public void testSearchSearcherCallback() {
		//Initialization of the searcher
		SimpleSearcherFactory targetSearcherFactory=new SimpleSearcherFactory(directory);
		MockSimpleSearcherFactory searcherFactory=new MockSimpleSearcherFactory(targetSearcherFactory);

		//Lucene template
		LuceneSearchTemplate template=new LuceneSearchTemplate(searcherFactory,new SimpleAnalyzer());
		String result=(String)template.search(new SearcherCallback() {
			public Object doWithSearcher(Searcher searcher) throws IOException, ParseException {
				assertNotNull(searcher);
				return "lucene";
			}
		});

		assertEquals(searcherFactory.getListener().getNumberSearchersCreated(),1);
		assertEquals(result,"lucene");
		assertEquals(searcherFactory.getListener().getNumberSearchersClosed(),1);
	}

}
