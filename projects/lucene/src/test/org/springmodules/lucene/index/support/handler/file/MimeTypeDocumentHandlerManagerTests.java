package org.springmodules.lucene.index.support.handler.file;

import junit.framework.TestCase;

import org.springmodules.lucene.index.DocumentHandlerException;
import org.springmodules.lucene.index.support.handler.DocumentHandlerManager;

public class MimeTypeDocumentHandlerManagerTests extends TestCase {

	public void testGetDocumentHandler() {
		DocumentHandlerManager manager=new MimeTypeDocumentHandlerManager();
		manager.registerDefaultHandlers();
		assertNotNull(manager.getDocumentHandler("test.txt"));
	}

	public void testGetDocumentHandlerNotFound() {
		DocumentHandlerManager manager=new MimeTypeDocumentHandlerManager();
		manager.registerDefaultHandlers();
		try {
			manager.getDocumentHandler("test.properties");
			fail();
		} catch (DocumentHandlerException e) {
		}
	}
}
