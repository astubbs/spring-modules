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

package org.springmodules.lucene.search.support;

import junit.framework.TestCase;

import org.apache.lucene.analysis.Analyzer;
import org.easymock.MockControl;
import org.easymock.classextension.MockClassControl;
import org.springmodules.lucene.search.core.DefaultLuceneSearchTemplate;
import org.springmodules.lucene.search.core.LuceneSearchTemplate;
import org.springmodules.lucene.search.factory.SearcherFactory;

public class LuceneSearchSupportTests extends TestCase {

	public void testIndexFactoryNotSpecified() throws Exception {
		LuceneSearchSupport luceneIndex = new LuceneSearchSupport() {};
		
		try {
			luceneIndex.afterPropertiesSet();
			fail();
		} catch(Exception ex) {}
	}

	public void testSearcherFactorySpecified() throws Exception {
		MockControl searcherFactoryControl = MockControl.createControl(SearcherFactory.class);
		SearcherFactory searcherFactory = (SearcherFactory)searcherFactoryControl.getMock();

		LuceneSearchSupport luceneSearch = new LuceneSearchSupport() {};
		luceneSearch.setSearcherFactory(searcherFactory);

		try {
			luceneSearch.afterPropertiesSet();
			fail();
		} catch(Exception ex) {}
	}

	public void testSearcherFactoryAnalyzerSpecified() throws Exception {
		MockControl searcherFactoryControl = MockControl.createControl(SearcherFactory.class);
		SearcherFactory searcherFactory = (SearcherFactory)searcherFactoryControl.getMock();
		MockControl analyzerControl = MockClassControl.createControl(Analyzer.class);
		Analyzer analyzer = (Analyzer)analyzerControl.getMock();

		LuceneSearchSupport luceneSearch = new LuceneSearchSupport() {};
		luceneSearch.setSearcherFactory(searcherFactory);
		luceneSearch.setAnalyzer(analyzer);
		
		luceneSearch.afterPropertiesSet();
	}

	public void testTemplateCreation() throws Exception {
		MockControl searcherFactoryControl = MockControl.createControl(SearcherFactory.class);
		SearcherFactory searcherFactory = (SearcherFactory)searcherFactoryControl.getMock();
		MockControl analyzerControl = MockClassControl.createControl(Analyzer.class);
		Analyzer analyzer = (Analyzer)analyzerControl.getMock();

		LuceneSearchSupport luceneSearch = new LuceneSearchSupport() {};
		luceneSearch.setSearcherFactory(searcherFactory);
		luceneSearch.setAnalyzer(analyzer);
		
		luceneSearch.afterPropertiesSet();
		
		LuceneSearchTemplate template = luceneSearch.getLuceneSearcherTemplate();
		assertNotNull(template);
		assertEquals(template.getClass(), DefaultLuceneSearchTemplate.class);
	}

	public void testTemplateInjection() throws Exception {
		MockControl luceneSearcherTemplateControl = MockControl.createControl(LuceneSearchTemplate.class);
		LuceneSearchTemplate luceneSearchTemplate = (LuceneSearchTemplate)luceneSearcherTemplateControl.getMock();

		LuceneSearchSupport luceneSearch = new LuceneSearchSupport() {};
		luceneSearch.setLuceneSearcherTemplate(luceneSearchTemplate);
		
		luceneSearch.afterPropertiesSet();
		
		LuceneSearchTemplate template = luceneSearch.getLuceneSearcherTemplate();
		assertNotNull(template);
		assertEquals(template.getClass(), luceneSearchTemplate.getClass());
	}
}
