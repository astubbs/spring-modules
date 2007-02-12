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

package org.springmodules.lucene.index.core;

import java.io.InputStream;
import java.util.Map;

import org.apache.lucene.document.Document;
import org.springmodules.lucene.index.document.handler.DocumentHandler;
import org.springmodules.lucene.index.document.handler.DocumentHandlerManager;

/**
 * Implementation of the DocumentCreator callback interface for creating a
 * Lucene document instance basing on an object and a DocumentHandlerManager.
 * So this is a specialization of the DocumentCreator class.
 *
 * <p>Used for input Document creation in LuceneIndexTemplate. Alternatively,
 * Document instances can be passed into LuceneIndexTemplate's corresponding
 * <code>addDocument</code> methods directly.
 *
 * <p>The aim of this DocumentCreator implementation is to integrate the use
 * of a DocumentHandlerManager instance for create a Lucene document from an
 * object (POJO). So you can declaratively configure the different document
 * handlers to use to index object using different strategies like introspection
 * or annotations.
 *
 * @author Thierry Templier
 * @see LuceneIndexTemplate#addDocument(DocumentCreator)
 * @see LuceneIndexTemplate#addDocument(DocumentCreator,org.apache.lucene.analysis.Analyzer)
 * @see org.springmodules.lucene.index.support.handler.DocumentHandlerManager
 * @see org.springmodules.lucene.index.support.handler.DocumentHandlerManagerFactoryBean
 */
public class DocumentCreatorWithManager implements DocumentCreator {

	private DocumentHandlerManager documentHandlerManager;
	private String name;
	private Object object;

	/**
	 * Construct a new DocumentCreatorWithManager, given a configured
	 * DocumentHandlerManager instance to index resources.
	 * 
	 * @param documentHandlerManager a configured DocumentHandlerManager instance to index resources
	 * @param object the object to use to create a Lucene document
	 */
	public DocumentCreatorWithManager(DocumentHandlerManager documentHandlerManager, Object object) {
		this(documentHandlerManager, object.getClass().getCanonicalName(), object);
	}

	/**
	 * Construct a new DocumentCreatorWithManager, given a configured
	 * DocumentHandlerManager instance to index resources.
	 * 
	 * @param documentHandlerManager a configured DocumentHandlerManager instance to index resources
	 * @param name the name associated with the object
	 * @param object the object to use to create a Lucene document
	 */
	public DocumentCreatorWithManager(DocumentHandlerManager documentHandlerManager, String name, Object object) {
		this.documentHandlerManager = documentHandlerManager;
		this.name = name;
		this.object = object;
	}

	/**
	 * This method implementes the mechanism to index a resource basing
	 * a DocumentHandlerManager instance.
	 * 
	 * <p>Firsly, it will try to get an available DocumentHandler corresponding
	 * to the class name . Then it calls the getDocument method of the DocumentHandler
	 * instance with the null description resource and the object in order to index
	 * the resource.
	 * 
	 * <p>If there is no DocumentHandler available for the class  name,
	 * the method will throw a DocumentHandlerException.
	 * 
	 * @return the Lucene Document instance
	 * @throws Exception if thrown by a Lucene method, to be auto-converted
	 * to a LuceneManipulateIndexException
	 * @see DocumentCreator#createDocument()
	 * @see #getResourceName()
	 * @see #getResourceDescription()
	 * @see DocumentHandlerManager#getDocumentHandler(String)
	 * @see DocumentHandler#getDocument(Map, InputStream)
	 * @see org.springmodules.lucene.index.DocumentHandlerException
	 */
	public final Document createDocument() throws Exception {
		DocumentHandler documentHandler = documentHandlerManager.getDocumentHandler(name);
		return documentHandler.getDocument(null, object);
	}

}
