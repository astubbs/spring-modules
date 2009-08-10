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

package org.springmodules.lucene.index.document.handler;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import junit.framework.TestCase;

import org.apache.lucene.document.Document;

public class DocumentHandlerManagerFactoryBeanTests extends TestCase {

	public void testDefaultConfigurationDocumentHandlerManager() throws Exception {
		DocumentHandlerManagerFactoryBean documentHandlerManagerFactoryBean = new DocumentHandlerManagerFactoryBean();
		Map documentHandlers = new HashMap();
		DocumentHandler documentHandler = new DocumentHandler() {
			public boolean supports(Class clazz) {
				return false;
			}

			public Document getDocument(Map description, Object object) throws Exception {
				return null;
			}
		};
		documentHandlers.put("test", documentHandler);
		documentHandlerManagerFactoryBean.setDocumentHandlers(documentHandlers);
		documentHandlerManagerFactoryBean.afterPropertiesSet();
		
		DocumentHandlerManager documentHandlerManager = (DocumentHandlerManager)documentHandlerManagerFactoryBean.getObject();
		assertEquals(documentHandlerManager.getClass(), DefaultDocumentHandlerManager.class);
		
		//An handler must match with test
		assertEquals(documentHandlerManager.getDocumentHandler("test"), documentHandler);

		//An handler must not match with test1
		try {
			documentHandlerManager.getDocumentHandler("test1");
			fail();
		} catch(Exception ex) {}
	}

	public void testSpecificConfigurationDocumentHandlerManager1() throws Exception {
		DocumentHandlerManagerFactoryBean documentHandlerManagerFactoryBean = new DocumentHandlerManagerFactoryBean();
		documentHandlerManagerFactoryBean.setDocumentHandlerManagerClass(TestDocumentHandlerManager.class);
		documentHandlerManagerFactoryBean.setDocumentMatchingClass(TestDocumentMatching.class);
		Map documentHandlers = new HashMap();
		DocumentHandler documentHandler = new DocumentHandler() {
			public boolean supports(Class clazz) {
				return false;
			}

			public Document getDocument(Map description, Object object) throws Exception {
				return null;
			}
		};
		documentHandlers.put("test", documentHandler);
		documentHandlerManagerFactoryBean.setDocumentHandlers(documentHandlers);
		documentHandlerManagerFactoryBean.afterPropertiesSet();
		
		DocumentHandlerManager documentHandlerManager = (DocumentHandlerManager)documentHandlerManagerFactoryBean.getObject();
		assertEquals(documentHandlerManager.getClass(), TestDocumentHandlerManager.class);
		
		//An handler must not match with test
		assertEquals(documentHandlerManager.getDocumentHandler("test"), null);
	}

	public void testSpecificConfigurationDocumentHandlerManager2() throws Exception {
		DocumentHandlerManagerFactoryBean documentHandlerManagerFactoryBean = new DocumentHandlerManagerFactoryBean();
		documentHandlerManagerFactoryBean.setDocumentHandlerManagerClass(TestDocumentHandlerManager.class);
		documentHandlerManagerFactoryBean.setDocumentMatchingClass(TestMatchAllDocumentMatching.class);
		Map documentHandlers = new HashMap();
		DocumentHandler documentHandler = new DocumentHandler() {
			public boolean supports(Class clazz) {
				return false;
			}

			public Document getDocument(Map description, Object object) throws Exception {
				return null;
			}
		};
		documentHandlers.put("test", documentHandler);
		documentHandlerManagerFactoryBean.setDocumentHandlers(documentHandlers);
		documentHandlerManagerFactoryBean.afterPropertiesSet();
		
		DocumentHandlerManager documentHandlerManager=(DocumentHandlerManager)documentHandlerManagerFactoryBean.getObject();
		assertEquals(documentHandlerManager.getClass(), TestDocumentHandlerManager.class);
		
		//An handler must not match with test
		assertEquals(documentHandlerManager.getDocumentHandler("test"), documentHandler);
	}

	private static class TestDocumentHandlerManager implements DocumentHandlerManager {
		private Map documentHandlers;
		public TestDocumentHandlerManager() {
			this.documentHandlers=new HashMap();
		}

		public DocumentHandler getDocumentHandler(String name) {
			Set keys = documentHandlers.keySet();
			for(Iterator i=keys.iterator(); i.hasNext();) {
				DocumentMatching matching = (DocumentMatching)i.next();
				if( matching.match(name) ) {
					return (DocumentHandler)documentHandlers.get(matching);
				}
			}
			return null;
		}

		public void registerDefaultHandlers() {
		}

		public void registerDocumentHandler(DocumentMatching matching, DocumentHandler handler) {
			if( matching!=null && handler!=null ) {
				documentHandlers.put(matching,handler);
			}
		}

		public void unregisterDocumentHandler(DocumentMatching matching) {
		}
	}
	
	private static class TestDocumentMatching implements DocumentMatching {
		public TestDocumentMatching(String pattern) {}

		public boolean match(String name) {
			return false;
		}
	}

	private static class TestMatchAllDocumentMatching implements DocumentMatching {
		public TestMatchAllDocumentMatching(String pattern) {}

		public boolean match(String name) {
			return true;
		}
	}
}
