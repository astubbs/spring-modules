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

package org.springmodules.lucene.search.factory;

import org.apache.lucene.analysis.SimpleAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.Searcher;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.store.RAMDirectory;
import org.springmodules.lucene.index.factory.IndexFactory;
import org.springmodules.lucene.index.factory.SimpleIndexFactory;
import org.springmodules.lucene.search.LuceneSearchException;

import junit.framework.TestCase;

/**
 * @author Thierry Templier
 */
public class SimpleSearcherFactoryTests extends TestCase {

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

	final public void testGetSearcher() throws Exception {
		SimpleSearcherFactory searcherFactory=new SimpleSearcherFactory();
		try {
			searcherFactory.getSearcher();
			fail();
		} catch(LuceneSearchException ex) {}
	}

	final public void testGetSearcherWithDirectory() throws Exception {
		SimpleSearcherFactory searcherFactory=new SimpleSearcherFactory();
		searcherFactory.setDirectory(directory);

		Searcher searcher=null;
		try {
			searcher=searcherFactory.getSearcher();
			assertNotNull(searcher);
			Hits hits=searcher.search(new TermQuery(new Term("field","sample")));
			assertEquals(hits.length(),3);
		} catch(Exception ex) {
			fail();
		} finally {
			if( searcher!=null ) {
				searcher.close();
			}
		}
	}

	final public void testGetSearcherWithIndexFactory() throws Exception {
		SimpleIndexFactory indexFactory=new SimpleIndexFactory();
		indexFactory.setDirectory(directory);

		SimpleSearcherFactory searcherFactory=new SimpleSearcherFactory();
		searcherFactory.setIndexFactory(indexFactory);

		Searcher searcher=null;
		try {
			searcher=searcherFactory.getSearcher();
			assertNotNull(searcher);
			Hits hits=searcher.search(new TermQuery(new Term("field","sample")));
			assertEquals(hits.length(),3);
		} catch(Exception ex) {
			fail();
		} finally {
			if( searcher!=null ) {
				searcher.close();
			}
		}
	}

}
