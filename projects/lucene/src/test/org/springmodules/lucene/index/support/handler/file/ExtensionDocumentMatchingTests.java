package org.springmodules.lucene.index.support.handler.file;

import junit.framework.TestCase;

import org.springmodules.lucene.index.support.handler.DocumentMatching;

public class ExtensionDocumentMatchingTests extends TestCase {
	public void testMatch() throws Exception {
		DocumentMatching documentMatching1 = new ExtensionDocumentMatching("txt");
		assertTrue(documentMatching1.match("test.txt"));

		DocumentMatching documentMatching2 = new ExtensionDocumentMatching("txt");
		assertTrue(documentMatching2.match("test.test.txt"));
	}

	public void testNotMatch() throws Exception {
		DocumentMatching documentMatching1 = new ExtensionDocumentMatching("doc");
		assertFalse(documentMatching1.match("test.txt"));

		DocumentMatching documentMatching2 = new ExtensionDocumentMatching("doc");
		assertFalse(documentMatching2.match("doc"));
	}
}
