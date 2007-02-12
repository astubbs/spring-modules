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

package org.springmodules.lucene.index.document.handler.file;

import junit.framework.TestCase;

import org.springmodules.lucene.index.document.handler.DocumentMatching;

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
