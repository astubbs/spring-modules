package org.springmodules.lucene.index.support.handler;

import junit.framework.TestCase;

public class AbstractRegexpDocumentMatchingTests extends TestCase {

	public void testCalls() throws Exception {
		final boolean[] called = {false};
		final String[] tabName = {null};
		DocumentMatching documentMatching=new AbstractRegexpDocumentMatching("test") {
			protected boolean matchRegularExpression(String name) {
				called[0]=true;
				tabName[0]=name;
				return false;
			}
			
		};

		assertFalse(documentMatching.match("un test"));
		assertTrue(called[0]);
		assertEquals("un test",tabName[0]);
	}
}
