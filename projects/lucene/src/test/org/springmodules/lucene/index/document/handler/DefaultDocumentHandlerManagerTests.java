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

import java.util.Map;

import junit.framework.TestCase;

import org.apache.lucene.document.Document;
import org.springmodules.lucene.index.DocumentHandlerException;

public class DefaultDocumentHandlerManagerTests extends TestCase {

	public void testGetDocumentHandler() {
		DocumentHandlerManager manager = new DefaultDocumentHandlerManager();
		DocumentHandler documentHandler = new DocumentHandler() {
			public boolean supports(Class clazz) {
				return false;
			}

			public Document getDocument(Map description, Object object) throws Exception {
				return null;
			}
		};
		manager.registerDocumentHandler(new IdentityDocumentMatching("test"), documentHandler);
		assertNotNull(manager.getDocumentHandler("test"));
	}

	public void testGetDocumentHandlerNotFound() {
		DocumentHandlerManager manager = new DefaultDocumentHandlerManager();
		DocumentHandler documentHandler = new DocumentHandler() {
			public boolean supports(Class clazz) {
				return false;
			}

			public Document getDocument(Map description, Object object) throws Exception {
				return null;
			}
		};
		manager.registerDocumentHandler(new IdentityDocumentMatching("test"), documentHandler);
		try {
			manager.getDocumentHandler("test1");
			fail();
		} catch (DocumentHandlerException ex) {
		}
	}

	public void testRegisterDocumentHandler() {
		DocumentHandlerManager manager=new DefaultDocumentHandlerManager();

		//No document handler
		try {
			manager.getDocumentHandler("test");
			fail();
		} catch (DocumentHandlerException ex) {
		}

		//Add a new one
		DocumentHandler documentHandler = new DocumentHandler() {
			public boolean supports(Class clazz) {
				return false;
			}

			public Document getDocument(Map description, Object object) throws Exception {
				return null;
			}
		};
		manager.registerDocumentHandler(new IdentityDocumentMatching("test"), documentHandler);

		//One document handler now
		assertNotNull(manager.getDocumentHandler("test"));
	}

	public void testUnregisterDocumentHandler() {
		DocumentHandlerManager manager = new DefaultDocumentHandlerManager();

		DocumentHandler documentHandler = new DocumentHandler() {
			public boolean supports(Class clazz) {
				return false;
			}

			public Document getDocument(Map description, Object object) throws Exception {
				return null;
			}
		};
		manager.registerDocumentHandler(new IdentityDocumentMatching("test"), documentHandler);

		//One document handler
		assertNotNull(manager.getDocumentHandler("test"));

		//Remove the document handler
		manager.unregisterDocumentHandler(new IdentityDocumentMatching("test"));

		//No document handler now
		try {
			manager.getDocumentHandler("test");
			fail();
		} catch (DocumentHandlerException ex) {
		}
	}

}
