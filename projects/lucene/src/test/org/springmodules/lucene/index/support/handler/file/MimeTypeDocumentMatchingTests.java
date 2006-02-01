package org.springmodules.lucene.index.support.handler.file;

import junit.framework.TestCase;

import org.springmodules.lucene.index.support.handler.DocumentMatching;

public class MimeTypeDocumentMatchingTests extends TestCase {
	public void testMatch() throws Exception {
		DocumentMatching documentMatching1=new MimeTypeDocumentMatching("text/plain");
		assertTrue(documentMatching1.match("test.txt"));

		DocumentMatching documentMatching2=new MimeTypeDocumentMatching("text/plain");
		assertTrue(documentMatching2.match("test.test.txt"));
	}

	public void testNotMatch() throws Exception {
		DocumentMatching documentMatching1=new MimeTypeDocumentMatching("doc");
		assertFalse(documentMatching1.match("test.txt"));

		DocumentMatching documentMatching2=new MimeTypeDocumentMatching("doc");
		assertFalse(documentMatching2.match("doc"));
	}
}
