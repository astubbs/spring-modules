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

package org.springmodules.lucene.search.factory;

import org.apache.lucene.index.Term;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.Searcher;
import org.apache.lucene.search.TermQuery;
import org.springmodules.lucene.AbstractLuceneTestCase;
import org.springmodules.lucene.index.factory.SimpleIndexFactory;
import org.springmodules.lucene.search.LuceneSearchException;

/**
 * @author Thierry Templier
 */
public class SimpleSearcherFactoryTests extends AbstractLuceneTestCase {

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
