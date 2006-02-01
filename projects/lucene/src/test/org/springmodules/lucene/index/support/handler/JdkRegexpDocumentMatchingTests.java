package org.springmodules.lucene.index.support.handler;

import junit.framework.TestCase;

public class JdkRegexpDocumentMatchingTests extends TestCase {
	public void testRegExpMatch() throws Exception {
		DocumentMatching documentMatching=new JdkRegexpDocumentMatching(".*un.*");
		assertTrue(documentMatching.match("un test"));
	}

	public void testRegExpNotMatch() throws Exception {
		DocumentMatching documentMatching=new JdkRegexpDocumentMatching(".*test.*");
		assertFalse(documentMatching.match("test"));
	}
}
