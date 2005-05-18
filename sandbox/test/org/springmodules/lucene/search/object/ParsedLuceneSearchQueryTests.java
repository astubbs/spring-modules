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

package org.springmodules.lucene.search.object;

import java.util.List;

import junit.framework.TestCase;

import org.apache.lucene.analysis.SimpleAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.RAMDirectory;
import org.springmodules.lucene.search.core.MockSimpleSearcherFactory;
import org.springmodules.lucene.search.factory.SimpleSearcherFactory;
import org.springmodules.lucene.search.query.ParsedQueryCreator.QueryParams;

/**
 * @author Thierry Templier
 */
public class ParsedLuceneSearchQueryTests extends TestCase {

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

	final public void testSearch() {
		//Initialization of the searcher
		SimpleSearcherFactory targetSearcherFactory=new SimpleSearcherFactory(directory);
		MockSimpleSearcherFactory searcherFactory=new MockSimpleSearcherFactory(targetSearcherFactory);

		//Initialization of the query
		LuceneSearchQuery query=new ParsedLuceneSearchQuery(searcherFactory,new SimpleAnalyzer()) {
			protected QueryParams configureSearchQuery(String textToSearch) {
				return new QueryParams("field",textToSearch);
			}

			protected Object extractResultHit(int id, Document document, float score) {
				return document.get("field");
			}
		};

		//First search
		List results=query.search("lucene");

		assertEquals(searcherFactory.getListener().getNumberSearchersCreated(),1);
		assertEquals(searcherFactory.getListener().getNumberSearchersClosed(),1);
		assertEquals(results.size(),1);
		assertEquals((String)results.get(0),"a Lucene support sample");

		//Second search
		results=query.search("sample");

		assertEquals(searcherFactory.getListener().getNumberSearchersCreated(),2);
		assertEquals(searcherFactory.getListener().getNumberSearchersClosed(),2);
		assertEquals(results.size(),3);
		assertEquals((String)results.get(0),"a sample");
		assertEquals((String)results.get(1),"a Lucene support sample");
		assertEquals((String)results.get(2),"a different sample");
	}
}
