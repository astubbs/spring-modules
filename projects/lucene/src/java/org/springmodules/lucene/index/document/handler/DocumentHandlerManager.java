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

/**
 * This is the base interface of the document handler
 * management. It allows to register/unregister them
 * and determines which document handler must be used
 * according to an associated DocumentMatching class.
 *  
 * @author Thierry Templier
 * @see org.springmodules.lucene.index.support.file.DocumentMatching
 * @see org.springmodules.lucene.index.support.file.DocumentHandler
 */
public interface DocumentHandlerManager {

	/**
	 * The method determines which document handler must be used
	 * for a name. It implicitely uses the document matching
	 * associated with the document handlers.
	 * 
	 * @param name the name associated with a resource to index
	 * @return the document handler to use
	 * @see DocumentMatching#match(String)
	 */
	DocumentHandler getDocumentHandler(String name);

	/**
	 * This method registers the default document handlers and
	 * must be called by the implementation during the initialization.
	 */
	void registerDefaultHandlers();

	/**
	 * This method registers a document handler associated with a
	 * document matching.
	 * 
	 * <p>The document matching paramter is used to determine when
	 * the handler must be used to index a document.
	 * 
	 * @param matching the associated document matching
	 * @param handler the document handler to register
	 */
	void registerDocumentHandler(DocumentMatching matching, DocumentHandler handler);

	/**
	 * This method is used to unregister a document handler associated
	 * with a document matching.
	 * 
	 * @param matching the associated document matching
	 */
	void unregisterDocumentHandler(DocumentMatching matching);
}
