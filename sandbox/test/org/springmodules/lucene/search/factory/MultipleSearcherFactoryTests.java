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

import java.io.IOException;

import junit.framework.TestCase;

import org.apache.lucene.analysis.SimpleAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.Searcher;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.springmodules.lucene.index.factory.IndexFactory;
import org.springmodules.lucene.index.factory.SimpleIndexFactory;
import org.springmodules.lucene.search.LuceneSearchException;

/**
 * @author Thierry Templier
 */
public class MultipleSearcherFactoryTests extends TestCase {

	private RAMDirectory directory1;
	private RAMDirectory directory2;

	private void setUpDirectory(Directory directory) throws IOException {
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
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		//Initialization of the indexes
		this.directory1=new RAMDirectory();
		setUpDirectory(this.directory1);
		this.directory2=new RAMDirectory();
		setUpDirectory(this.directory2);
	}

	final public void testGetSearcher() throws Exception {
		MultipleSearcherFactory searcherFactory=new MultipleSearcherFactory();
		try {
			searcherFactory.getSearcher();
			fail();
		} catch(LuceneSearchException ex) {}
	}

	final public void testGetSearcherWithDirectories() throws Exception {
		MultipleSearcherFactory searcherFactory=new MultipleSearcherFactory();
		searcherFactory.setDirectories(new Directory[] {directory1,directory2});

		Searcher searcher=null;
		try {
			searcher=searcherFactory.getSearcher();
			assertNotNull(searcher);
			Hits hits=searcher.search(new TermQuery(new Term("field","sample")));
			assertEquals(hits.length(),6);
		} catch(Exception ex) {
			ex.printStackTrace();
			fail();
		} finally {
			if( searcher!=null ) {
				searcher.close();
			}
		}
	}

	final public void testGetSearcherWithIndexFactories() throws Exception {
		SimpleIndexFactory indexFactory1=new SimpleIndexFactory();
		indexFactory1.setDirectory(directory1);
		SimpleIndexFactory indexFactory2=new SimpleIndexFactory();
		indexFactory2.setDirectory(directory2);

		MultipleSearcherFactory searcherFactory=new MultipleSearcherFactory();
		searcherFactory.setIndexFactories(new IndexFactory[] {indexFactory1,indexFactory2});

		Searcher searcher=null;
		try {
			searcher=searcherFactory.getSearcher();
			assertNotNull(searcher);
			Hits hits=searcher.search(new TermQuery(new Term("field","sample")));
			assertEquals(hits.length(),6);
		} catch(Exception ex) {
			ex.printStackTrace();
			fail();
		} finally {
			if( searcher!=null ) {
				searcher.close();
			}
		}
	}

	final public void testGetSearcherWithDirectoryAndIndexFactory() throws Exception {
		SimpleIndexFactory indexFactory1=new SimpleIndexFactory();
		indexFactory1.setDirectory(directory1);

		MultipleSearcherFactory searcherFactory=new MultipleSearcherFactory();
		searcherFactory.setIndexFactories(new IndexFactory[] {indexFactory1});
		searcherFactory.setDirectories(new Directory[] {directory2});

		Searcher searcher=null;
		try {
			searcher=searcherFactory.getSearcher();
			assertNotNull(searcher);
			Hits hits=searcher.search(new TermQuery(new Term("field","sample")));
			assertEquals(hits.length(),6);
		} catch(Exception ex) {
			ex.printStackTrace();
			fail();
		} finally {
			if( searcher!=null ) {
				searcher.close();
			}
		}
	}

}
