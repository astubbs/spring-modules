package org.springmodules.lucene.index.support.handler;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.lucene.document.Document;

import junit.framework.TestCase;

public class DocumentHandlerManagerFactoryBeanTests extends TestCase {

	public void testDefaultConfigurationDocumentHandlerManager() throws Exception {
		DocumentHandlerManagerFactoryBean documentHandlerManagerFactoryBean=new DocumentHandlerManagerFactoryBean();
		Map documentHandlers=new HashMap();
		DocumentHandler documentHandler=new DocumentHandler() {
			public boolean supports(Class clazz) {
				return false;
			}

			public Document getDocument(Map description, Object object) throws Exception {
				return null;
			}
		};
		documentHandlers.put("test",documentHandler);
		documentHandlerManagerFactoryBean.setDocumentHandlers(documentHandlers);
		documentHandlerManagerFactoryBean.afterPropertiesSet();
		
		DocumentHandlerManager documentHandlerManager=(DocumentHandlerManager)documentHandlerManagerFactoryBean.getObject();
		assertEquals(documentHandlerManager.getClass(),DefaultDocumentHandlerManager.class);
		
		//An handler must match with test
		assertEquals(documentHandlerManager.getDocumentHandler("test"),documentHandler);

		//An handler must not match with test1
		try {
			documentHandlerManager.getDocumentHandler("test1");
			fail();
		} catch(Exception ex) {}
	}

	public void testSpecificConfigurationDocumentHandlerManager1() throws Exception {
		DocumentHandlerManagerFactoryBean documentHandlerManagerFactoryBean=new DocumentHandlerManagerFactoryBean();
		documentHandlerManagerFactoryBean.setDocumentHandlerManagerClass(TestDocumentHandlerManager.class);
		documentHandlerManagerFactoryBean.setDocumentMatchingClass(TestDocumentMatching.class);
		Map documentHandlers=new HashMap();
		DocumentHandler documentHandler=new DocumentHandler() {
			public boolean supports(Class clazz) {
				return false;
			}

			public Document getDocument(Map description, Object object) throws Exception {
				return null;
			}
		};
		documentHandlers.put("test",documentHandler);
		documentHandlerManagerFactoryBean.setDocumentHandlers(documentHandlers);
		documentHandlerManagerFactoryBean.afterPropertiesSet();
		
		DocumentHandlerManager documentHandlerManager=(DocumentHandlerManager)documentHandlerManagerFactoryBean.getObject();
		assertEquals(documentHandlerManager.getClass(),TestDocumentHandlerManager.class);
		
		//An handler must not match with test
		assertEquals(documentHandlerManager.getDocumentHandler("test"),null);
	}

	public void testSpecificConfigurationDocumentHandlerManager2() throws Exception {
		DocumentHandlerManagerFactoryBean documentHandlerManagerFactoryBean=new DocumentHandlerManagerFactoryBean();
		documentHandlerManagerFactoryBean.setDocumentHandlerManagerClass(TestDocumentHandlerManager.class);
		documentHandlerManagerFactoryBean.setDocumentMatchingClass(TestMatchAllDocumentMatching.class);
		Map documentHandlers=new HashMap();
		DocumentHandler documentHandler=new DocumentHandler() {
			public boolean supports(Class clazz) {
				return false;
			}

			public Document getDocument(Map description, Object object) throws Exception {
				return null;
			}
		};
		documentHandlers.put("test",documentHandler);
		documentHandlerManagerFactoryBean.setDocumentHandlers(documentHandlers);
		documentHandlerManagerFactoryBean.afterPropertiesSet();
		
		DocumentHandlerManager documentHandlerManager=(DocumentHandlerManager)documentHandlerManagerFactoryBean.getObject();
		assertEquals(documentHandlerManager.getClass(),TestDocumentHandlerManager.class);
		
		//An handler must not match with test
		assertEquals(documentHandlerManager.getDocumentHandler("test"),documentHandler);
	}

	private static class TestDocumentHandlerManager implements DocumentHandlerManager {
		private Map documentHandlers;
		public TestDocumentHandlerManager() {
			this.documentHandlers=new HashMap();
		}

		public DocumentHandler getDocumentHandler(String name) {
			Set keys=documentHandlers.keySet();
			for(Iterator i=keys.iterator();i.hasNext();) {
				DocumentMatching matching=(DocumentMatching)i.next();
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
