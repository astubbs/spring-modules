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
import java.util.Map;

import org.apache.lucene.document.Document;

import junit.framework.TestCase;

public class AbstractDocumentHandlerTests extends TestCase {

	public void testGetDocumentSupport() throws Exception {
		final boolean[] called = { false, false };
		final Class[] tabClazz = { null };
		DocumentHandler documentHandler=new AbstractDocumentHandler() {
			public boolean supports(Class clazz) {
				called[0] = true;
				tabClazz[0] = clazz;
				return true;
			}
			
			protected Document doGetDocument(Map description, Object object) throws Exception {
				called[1] = true;
				return null;
			}
		};

		documentHandler.getDocument(new HashMap(), "test");

		assertTrue(called[0]);
		assertTrue(called[1]);
		assertEquals(String.class, tabClazz[0]);
	}

	public void testGetDocumentNotSupport() throws Exception {
		final boolean[] called = { false, false };
		final Class[] tabClazz = { null };
		DocumentHandler documentHandler = new AbstractDocumentHandler() {
			public boolean supports(Class clazz) {
				called[0] = true;
				tabClazz[0] = clazz;
				return (clazz==Map.class);
			}
			
			protected Document doGetDocument(Map description, Object object) throws Exception {
				called[1] = true;
				return null;
			}
		};

		try {
			documentHandler.getDocument(new HashMap(), "test");
			fail();
		} catch (Exception ex) {
			assertTrue(called[0]);
			assertFalse(called[1]);
			assertEquals(String.class, tabClazz[0]);
		}
	}
}
