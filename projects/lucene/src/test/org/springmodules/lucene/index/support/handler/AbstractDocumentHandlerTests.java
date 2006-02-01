package org.springmodules.lucene.index.support.handler;

import java.util.HashMap;
import java.util.Map;

import org.apache.lucene.document.Document;

import junit.framework.TestCase;

public class AbstractDocumentHandlerTests extends TestCase {

	public void testGetDocumentSupport() throws Exception {
		final boolean[] called = {false,false};
		final Class[] tabClazz = {null};
		DocumentHandler documentHandler=new AbstractDocumentHandler() {
			public boolean supports(Class clazz) {
				called[0]=true;
				tabClazz[0]=clazz;
				return true;
			}
			
			protected Document doGetDocument(Map description, Object object) throws Exception {
				called[1]=true;
				return null;
			}
		};

		documentHandler.getDocument(new HashMap(),"test");

		assertTrue(called[0]);
		assertTrue(called[1]);
		assertEquals(String.class,tabClazz[0]);
	}

	public void testGetDocumentNotSupport() throws Exception {
		final boolean[] called = {false,false};
		final Class[] tabClazz = {null};
		DocumentHandler documentHandler=new AbstractDocumentHandler() {
			public boolean supports(Class clazz) {
				called[0]=true;
				tabClazz[0]=clazz;
				return (clazz==Map.class);
			}
			
			protected Document doGetDocument(Map description, Object object) throws Exception {
				called[1]=true;
				return null;
			}
		};

		try {
			documentHandler.getDocument(new HashMap(),"test");
			fail();
		} catch (Exception e) {
			assertTrue(called[0]);
			assertFalse(called[1]);
			assertEquals(String.class,tabClazz[0]);
		}
	}
}
