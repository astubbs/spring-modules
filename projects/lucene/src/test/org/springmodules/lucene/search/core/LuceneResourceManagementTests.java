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
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.springmodules.lucene.AbstractLuceneTestCase;
import org.springmodules.lucene.search.factory.SimpleSearcherFactory;
import org.springmodules.resource.ResourceManager;
import org.springmodules.resource.support.ResourceBindingManager;
import org.springmodules.resource.support.ResourceCallback;
import org.springmodules.resource.support.ResourceTemplate;

/**
 * @author Thierry Templier
 */
public class LuceneResourceManagementTests extends AbstractLuceneTestCase {

	public void testLuceneReaderResourceManagement() {
		//Initialization of the searcher
		SimpleSearcherFactory targetSearcherFactory=new SimpleSearcherFactory(directory);
		final MockSimpleSearcherFactory searcherFactory=new MockSimpleSearcherFactory(targetSearcherFactory);

		ResourceManager rm=new LuceneSearcherResourceManager(searcherFactory);
		ResourceTemplate rt = new ResourceTemplate(rm);

		List results=(List)rt.execute(new ResourceCallback() {
			public Object doWithResource() {
				assertTrue("Has thread indexFactory", ResourceBindingManager.hasResource(searcherFactory));

				//Lucene template
				LuceneSearchTemplate template=new DefaultLuceneSearchTemplate(searcherFactory,new SimpleAnalyzer());

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

				return results;
			}
		});

		assertEquals(searcherFactory.getListener().getNumberSearchersCreated(),1);
		assertEquals(results.size(),3);
		assertEquals((String)results.get(0),"a sample");
		assertEquals((String)results.get(1),"a Lucene support sample");
		assertEquals((String)results.get(2),"a different sample");
		assertEquals(searcherFactory.getListener().getNumberSearchersClosed(),1);

	}

	public void testNoLuceneReaderResourceManagement() {
		//Initialization of the searcher
		SimpleSearcherFactory targetSearcherFactory=new SimpleSearcherFactory(directory);
		final MockSimpleSearcherFactory searcherFactory=new MockSimpleSearcherFactory(targetSearcherFactory);

		ResourceManager rm=new LuceneSearcherResourceManager(searcherFactory);
		ResourceTemplate rt = new ResourceTemplate(rm);

		//Lucene template
		LuceneSearchTemplate template=new DefaultLuceneSearchTemplate(searcherFactory,new SimpleAnalyzer());

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
		assertEquals(results.size(),3);
		assertEquals((String)results.get(0),"a sample");
		assertEquals((String)results.get(1),"a Lucene support sample");
		assertEquals((String)results.get(2),"a different sample");
		assertEquals(searcherFactory.getListener().getNumberSearchersClosed(),2);

	}

}
