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

package org.springmodules.lucene.index.support;

import org.easymock.MockControl;
import org.springmodules.lucene.index.core.DefaultLuceneIndexTemplate;
import org.springmodules.lucene.index.core.LuceneIndexTemplate;
import org.springmodules.lucene.index.factory.IndexFactory;

import junit.framework.TestCase;

public class LuceneIndexSupportTests extends TestCase {

	public void testIndexFactoryNotSpecified() throws Exception {
		LuceneIndexSupport luceneIndex = new LuceneIndexSupport() {};
		
		try {
			luceneIndex.afterPropertiesSet();
			fail();
		} catch(Exception ex) {}
	}

	public void testIndexFactorySpecified() throws Exception {
		MockControl indexFactoryControl = MockControl.createControl(IndexFactory.class);
		IndexFactory indexFactory = (IndexFactory)indexFactoryControl.getMock();

		LuceneIndexSupport luceneIndex = new LuceneIndexSupport() {};
		luceneIndex.setIndexFactory(indexFactory);
		
		luceneIndex.afterPropertiesSet();
	}

	public void testTemplateCreation() throws Exception {
		MockControl indexFactoryControl = MockControl.createControl(IndexFactory.class);
		IndexFactory indexFactory = (IndexFactory)indexFactoryControl.getMock();

		LuceneIndexSupport luceneIndex = new LuceneIndexSupport() {};
		luceneIndex.setIndexFactory(indexFactory);
		
		luceneIndex.afterPropertiesSet();
		
		LuceneIndexTemplate template = luceneIndex.getTemplate();
		assertNotNull(template);
		assertEquals(template.getClass(), DefaultLuceneIndexTemplate.class);
	}

	public void testTemplateInjection() throws Exception {
		MockControl luceneIndexTemplateControl = MockControl.createControl(LuceneIndexTemplate.class);
		LuceneIndexTemplate luceneIndexTemplate = (LuceneIndexTemplate)luceneIndexTemplateControl.getMock();

		LuceneIndexSupport luceneIndex = new LuceneIndexSupport() {};
		luceneIndex.setTemplate(luceneIndexTemplate);
		
		luceneIndex.afterPropertiesSet();
		
		LuceneIndexTemplate template = luceneIndex.getTemplate();
		assertNotNull(template);
		assertEquals(template.getClass(), luceneIndexTemplate.getClass());
	}
}
