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
import org.springmodules.samples.lucene.index.file.handlers.AbstractDocumentHandler;

/**
 * This class is the default implementation of the DocumentHandlerManager
 * interface which allows to manage document handlers (registration,
 * unregistration, getting).
 * 
 * @author Thierry Templier
 */
public class DefaultDocumentHandlerManager implements DocumentHandlerManager {
	private Map documentHandlers;

	/**
	 * Construct a new DefaultDocumentHandlerManager. It initializes the
	 * internal Map which will be used to store the registered document
	 * handlers.
	 */
	public DefaultDocumentHandlerManager() {
		this.documentHandlers=new HashMap();
	}

	/**
	 * This method is used to register the default document
	 * handlers. In the context of the DefaultDocumentHandlerManager
	 * class, there is non default handler registered.
	 * @see org.springmodules.lucene.index.support.file.DocumentHandlerManager#registerDefautHandlers()
	 */
	public void registerDefautHandlers() {
	}

	/**
	 * The method determines which document handler must be used
	 * for a name. It implicitely uses the document matching
	 * associated with the document handlers.
	 * 
	 * @param name the name associated with a resource to index
	 * @return the document handler to use
	 * @see DocumentMatching#match(String)
	 * @see org.springmodules.lucene.index.object.file.DocumentHandlerManager#getDocumentHandler(java.lang.String)
	 */
	public DocumentHandler getDocumentHandler(String fileName) {
		Set keys=documentHandlers.keySet();
		for(Iterator i=keys.iterator();i.hasNext();) {
			DocumentMatching matching=(DocumentMatching)i.next();
			if( matching.match(fileName) ) {
				return (DocumentHandler)documentHandlers.get(matching);
			}
		}
		return null;
	}

	/**
	 * This method is used to register a document handler associated
	 * with a document matching.
	 * 
	 * <p>The document matching is used to determine when the handler
	 * must be used to index a document.
	 * 
	 * @param matching the associated document matching
	 * @param handler the document handler to register
	 * @see org.springmodules.lucene.index.object.file.DocumentHandlerManager#registerDocumentHandler(org.springmodules.lucene.index.object.file.DocumentMatching, org.springmodules.lucene.index.object.file.DocumentHandler)
	 */
	public void registerDocumentHandler(DocumentMatching matching, DocumentHandler handler) {
		if( matching!=null && handler!=null ) {
			documentHandlers.put(matching,handler);
		}
	}

	/**
	 * This method is used to unregister a document handler associated
	 * with a document matching.
	 * 
	 * @param matching the associated document matching
	 * @see org.springmodules.lucene.index.object.file.DocumentHandlerManager#unregisterDocumentHandler(org.springmodules.lucene.index.object.file.DocumentMatching)
	 */
	public void unregisterDocumentHandler(DocumentMatching matching) {
		if( matching!=null ) {
			documentHandlers.remove(matching);
		}
	}

}
