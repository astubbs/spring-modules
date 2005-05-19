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

package org.springmodules.lucene.index.core;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.lucene.document.Document;
import org.springmodules.lucene.index.support.file.DocumentHandler;
import org.springmodules.lucene.index.support.file.DocumentHandlerManager;
import org.springmodules.samples.lucene.index.FileExtensionNotSupportedException;

/**
 * Implementation of the DocumentCreator callback interface for creating a
 * Lucene document instance basing on an InputStream and a DocumentHandlerManager.
 * So this is a specialization of the InputStreamDocumentCreator class.
 *
 * <p>Used for input Document creation in LuceneIndexTemplate. Alternatively,
 * Document instances can be passed into LuceneIndexTemplate's corresponding
 * <code>addDocument</code> methods directly.
 *
 * <p>The aim of this DocumentCreator implementation is to integrate the use
 * of a DocumentHandlerManager instance for create a Lucene document from an
 * InputStream. So you can declaratively configure the different document
 * handlers to use to index resources (file, stream...).
 *
 * @author Thierry Templier
 * @see LuceneIndexTemplate#addDocument(DocumentCreator)
 * @see LuceneIndexTemplate#addDocument(DocumentCreator,org.apache.lucene.analysis.Analyzer)
 * @see org.springmodules.lucene.index.support.file.DocumentHandlerManager
 * @see org.springmodules.lucene.index.support.file.ExtensionDocumentHandlerManagerFactoryBean
 */
public abstract class InputStreamDocumentCreatorWithManager extends InputStreamDocumentCreator {

	private DocumentHandlerManager documentHandlerManager;

	/**
	 * Construct a new InputStreamDocumentCreatorWithManager, given a configured
	 * DocumentHandlerManager instance to index resources.
	 * 
	 * @param indexFactory a configured DocumentHandlerManager instance to index resources
	 */
	public InputStreamDocumentCreatorWithManager(DocumentHandlerManager documentHandlerManager) {
		this.documentHandlerManager=documentHandlerManager;
	}

	/**
	 * This method returns the name of the resource to index. This name
	 * will be used by the class to select the DocumentHandler which
	 * matches in order to index it.
	 * @return the resource name
	 */
	protected abstract String getResourceName();

	/**
	 * This method returns the description of the resource to index. This
	 * description will be used as parameter to the getDocument method of
	 * the selected DocumentHandler.
	 * @return the Lucene Document instance
	 * @throws IOException if thrown by a Lucene method, to be auto-converted
	 * to a LuceneManipulateIndexException
	 */
	protected abstract Map getResourceDescription();

	/**
	 * This method implementes the mechanism to index a resource basing
	 * a DocumentHandlerManager instance.
	 * 
	 * <p>Firsly, it will try to get an available DocumentHandler corresponding
	 * to the resource name returned by the getResourceName method. Then it
	 * calls the getDocument method of the DocumentHandler instance with the
	 * description resource returned by the getResourceDescription method in
	 * order to index the resource.
	 * 
	 * <p>If there is no DocumentHandler available for the resource name,
	 * the method will throw a FileExtensionNotSupportedException.
	 * 
	 * @param inputStream the InputStream on the resource to index
	 * @return the Lucene Document instance
	 * @throws IOException if thrown by a Lucene method, to be auto-converted
	 * to a LuceneManipulateIndexException
	 * @see org.springmodules.lucene.index.core.InputStreamDocumentCreator#createDocumentFromInputStream(java.io.InputStream)
	 * @see #getResourceName()
	 * @see #getResourceDescription()
	 * @see DocumentHandlerManager#getDocumentHandler(String)
	 * @see DocumentHandler#getDocument(Map, InputStream)
	 * @see FileExtensionNotSupportedException
	 */
	protected final Document createDocumentFromInputStream(
	                          InputStream inputStream) throws IOException {
		DocumentHandler documentHandler=documentHandlerManager.getDocumentHandler(getResourceName());
		if( documentHandler!=null ) {
			return documentHandler.getDocument(getResourceDescription(),inputStream);
		} else {
			throw new FileExtensionNotSupportedException("No handler for this resource name");
		}
	}

}
