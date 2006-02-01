package org.springmodules.lucene.index.support.handler;

import java.util.Map;

import org.apache.lucene.document.Document;
import org.springmodules.lucene.index.DocumentHandlerException;

import junit.framework.TestCase;

public class DefaultDocumentHandlerManagerTests extends TestCase {

	public void testGetDocumentHandler() {
		DocumentHandlerManager manager=new DefaultDocumentHandlerManager();
		DocumentHandler documentHandler=new DocumentHandler() {
			public boolean supports(Class clazz) {
				return false;
			}

			public Document getDocument(Map description, Object object) throws Exception {
				return null;
			}
		};
		manager.registerDocumentHandler(new IdentityDocumentMatching("test"),documentHandler);
		assertNotNull(manager.getDocumentHandler("test"));
	}

	public void testGetDocumentHandlerNotFound() {
		DocumentHandlerManager manager=new DefaultDocumentHandlerManager();
		DocumentHandler documentHandler=new DocumentHandler() {
			public boolean supports(Class clazz) {
				return false;
			}

			public Document getDocument(Map description, Object object) throws Exception {
				return null;
			}
		};
		manager.registerDocumentHandler(new IdentityDocumentMatching("test"),documentHandler);
		try {
			manager.getDocumentHandler("test1");
			fail();
		} catch (DocumentHandlerException e) {
		}
	}

	public void testRegisterDocumentHandler() {
		DocumentHandlerManager manager=new DefaultDocumentHandlerManager();

		//No document handler
		try {
			manager.getDocumentHandler("test");
			fail();
		} catch (DocumentHandlerException e) {
		}

		//Add a new one
		DocumentHandler documentHandler=new DocumentHandler() {
			public boolean supports(Class clazz) {
				return false;
			}

			public Document getDocument(Map description, Object object) throws Exception {
				return null;
			}
		};
		manager.registerDocumentHandler(new IdentityDocumentMatching("test"),documentHandler);

		//One document handler now
		assertNotNull(manager.getDocumentHandler("test"));
	}

	public void testUnregisterDocumentHandler() {
		DocumentHandlerManager manager=new DefaultDocumentHandlerManager();

		DocumentHandler documentHandler=new DocumentHandler() {
			public boolean supports(Class clazz) {
				return false;
			}

			public Document getDocument(Map description, Object object) throws Exception {
				return null;
			}
		};
		manager.registerDocumentHandler(new IdentityDocumentMatching("test"),documentHandler);

		//One document handler
		assertNotNull(manager.getDocumentHandler("test"));

		//Remove the document handler
		manager.unregisterDocumentHandler(new IdentityDocumentMatching("test"));

		//No document handler now
		try {
			manager.getDocumentHandler("test");
			fail();
		} catch (DocumentHandlerException e) {
		}
	}

}
