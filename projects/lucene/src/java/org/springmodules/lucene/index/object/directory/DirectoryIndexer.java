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

package org.springmodules.lucene.index.object.directory;

import org.springmodules.lucene.index.document.handler.DocumentHandler;
import org.springmodules.lucene.index.document.handler.DocumentMatching;

/**
 * @author Thierry Templier
 */
public interface DirectoryIndexer {

	/**
	 * This method is used to register a document handler. It is associated to
	 * a DocumentMatching instance to determine when this handler must be used.
	 * As the indexer works on directories and files, it is used to determine
	 * which document handler will be used to index a file.
	 * 
	 * @param matching the DocumentMatching associated with the handler
	 * @param handler the handler to register
	 */
	void registerDocumentHandler(DocumentMatching matching, DocumentHandler handler);

	/**
	 * This method is used to unregister the document handler corresponding the
	 * mapping.
	 * 
	 * @param matching the DocumentMatching associated with the handler
	 */
	void unregisterDocumentHandler(DocumentMatching matching);

	/**
	 * This method is used to add a listener to be notified during the
	 * indexing execution.
	 * 
	 * @param listener the listener to add
	 */
	void addListener(FileDocumentIndexingListener listener);

	/**
	 * This method is used to remove a specified listener.
	 * 
	 * @param listener the listener to remove
	 */
	void removeListener(FileDocumentIndexingListener listener);

	/**
	 * This method is the entry point to index a directory recursively. It uses
	 * the registred document handlers to index every files.
	 * 
	 * <p>In this case, the index will not be optimized.
	 *  
	 * @param dirToParse the directory to index recursively
	 */
	void index(String dirToParse);

	/**
	 * This method is the entry point to index a directory recursively. It uses
	 * the registred document handlers to index every files.
	 * 
	 * <p>In this case, the index will be optimized after each request if the
	 * value of the optimizeIndex parameter is true.
	 * 
	 * <p>If there is an error during executing a file, the other files will be
	 * executed. However the error will notify to specified listeners.
	 *  
	 * <p>This method gets an IndexWriter instance from the IndexWriterFactoryUtils
	 * class and release it at the end if necessary. 
	 * 
	 * @param dirToParse the directory to index recursively
	 * @param optimizeIndex if the index must be optimized after
	 * the request indexing
	 */
	void index(String dirToParse, boolean optimizeIndex);
}