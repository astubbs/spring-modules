package org.springmodules.lucene.index.support.handler;

import junit.framework.TestCase;

public class IdentityDocumentMatchingTests extends TestCase {
	public void testMatch() throws Exception {
		DocumentMatching documentMatching=new IdentityDocumentMatching("test");
		assertTrue(documentMatching.match("test"));
	}

	public void testMatchAll() throws Exception {
		DocumentMatching documentMatching=new IdentityDocumentMatching("*");
		assertTrue(documentMatching.match("test"));
		assertTrue(documentMatching.match("test1"));
		assertTrue(documentMatching.match("1test"));
		assertTrue(documentMatching.match("te1st"));
	}

	public void testNotMatch() throws Exception {
		DocumentMatching documentMatching=new IdentityDocumentMatching("test");
		assertFalse(documentMatching.match("test1"));
	}
}
