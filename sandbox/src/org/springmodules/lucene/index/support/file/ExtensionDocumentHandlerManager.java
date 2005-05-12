/*
 * Copyright 2002-2005 the original author or authors.
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

package org.springmodules.lucene.index.support.file;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.springmodules.lucene.index.support.file.handler.TextDocumentHandler;

/**
 * @author Thierry Templier
 */
public class ExtensionDocumentHandlerManager implements DocumentHandlerManager {
	private Map fileDocumentHandlers;

	public ExtensionDocumentHandlerManager() {
		this.fileDocumentHandlers=new HashMap();
	}

	/**
	 * @see org.springmodules.lucene.index.object.file.DocumentHandlerManager#getDocumentHandler(java.lang.String)
	 */
	public DocumentHandler getDocumentHandler(String fileName) {
		Set keys=fileDocumentHandlers.keySet();
		for(Iterator i=keys.iterator();i.hasNext();) {
			DocumentMatching matching=(DocumentMatching)i.next();
			if( matching.match(fileName) ) {
				return (DocumentHandler)fileDocumentHandlers.get(matching);
			}
		}
		return null;
	}

	/**
	 * @see org.springmodules.lucene.index.object.file.DocumentHandlerManager#registerDefautHandlers()
	 */
	public void registerDefautHandlers() {
		//Register a default handler for text file (.txt)
		registerDocumentHandler(new DocumentExtensionMatching("txt"),new TextDocumentHandler());
	}

	/**
	 * @see org.springmodules.lucene.index.object.file.DocumentHandlerManager#registerDocumentHandler(org.springmodules.lucene.index.object.file.DocumentMatching, org.springmodules.lucene.index.object.file.DocumentHandler)
	 */
	public void registerDocumentHandler(DocumentMatching matching, DocumentHandler handler) {
		if( matching!=null && handler!=null ) {
			fileDocumentHandlers.put(matching,handler);
		}
	}

	/**
	 * @see org.springmodules.lucene.index.object.file.DocumentHandlerManager#unregisterDocumentHandler(org.springmodules.lucene.index.object.file.DocumentMatching)
	 */
	public void unregisterDocumentHandler(DocumentMatching matching) {
		if( matching!=null ) {
			fileDocumentHandlers.remove(matching);
		}
	}

}
