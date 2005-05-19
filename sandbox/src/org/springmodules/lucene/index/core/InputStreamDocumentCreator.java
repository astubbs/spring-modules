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

import org.apache.lucene.document.Document;
import org.springmodules.lucene.util.IOUtils;

/**
 * Implementation of the DocumentCreator callback interface for creating a
 * Lucene document instance basing on an InputStream.
 *
 * <p>Used for input Document creation in LuceneIndexTemplate. Alternatively,
 * Document instances can be passed into LuceneIndexTemplate's corresponding
 * <code>addDocument</code> methods directly.
 *
 * <p>The aim of this DocumentCreator implementation is to facilitate the
 * manipulation of the InputStream of the resource to index. To use this
 * implementation, you need to specify how to create the InputStream and
 * how to create a Lucene document from an InputStream.
 *
 * @author Thierry Templier
 * @see LuceneIndexTemplate#addDocument(DocumentCreator)
 * @see LuceneIndexTemplate#addDocument(DocumentCreator,org.apache.lucene.analysis.Analyzer)
 */
public abstract class InputStreamDocumentCreator implements DocumentCreator {

	/**
	 * This method must be implemented to specify how to create
	 * the InputStream on the resource to index.
	 * 
	 * @return the InputStream on the resource to index
	 * @throws IOException if thrown by an IO method, to be auto-converted
	 * to a LuceneManipulateIndexException
	 */
	protected abstract InputStream createInputStream() throws IOException;

	/**
	 * This method must be implemented to specify how to index
	 * a document from the created InputStream by the createInputStream
	 * method.
	 * @param inputStream the InputStream on the resource to index
	 * @return the Lucene Document instance
	 * @throws IOException if thrown by a Lucene method, to be auto-converted
	 * to a LuceneManipulateIndexException
	 */
	protected abstract Document createDocumentFromInputStream(
	                          InputStream inputStream) throws IOException;

	/**
	 * This method is based on the createInputStream and createDocumentFromInputStream
	 * methods to hide the InputStream manipulation of the resource to index.
	 * @return the Lucene Document instance
	 * @see #createInputStream()
	 * @see #createDocumentFromInputStream(InputStream)
	 * @see org.springmodules.lucene.index.core.DocumentCreator#createDocument()
	 * @throws IOException if thrown by a Lucene method, to be auto-converted
	 * to a LuceneManipulateIndexException
	 */
	public final Document createDocument() throws IOException {
		InputStream inputStream=null;
		try {
			inputStream=createInputStream();
			return createDocumentFromInputStream(inputStream);
		} finally {
			IOUtils.closeInputStream(inputStream);
		}
	}

}
