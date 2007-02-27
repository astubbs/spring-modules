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

import java.io.IOException;
import java.io.InputStream;

import org.apache.lucene.document.Document;

/**
 * Callback interface for creating a Lucene document instance from an
 * InputStream.
 *
 * <p>Used for input Document creation in LuceneIndexTemplate. Alternatively,
 * Document instances can be passed into LuceneIndexTemplate's corresponding
 * <code>addDocument</code> methods directly but the application must manage
 * the opening/closing of the InputStream.
 *
 * <p><b>Caution</b>: You cannot use the DocumentCreator if you want to index datas
 * from an InputStream. If you use an InputStream with a DocumentCreator, you
 * need to manage the stream outside the template and don't close it in the
 * createDocument method because the stream must be still opened when the
 * addDocument is called.
 *
 * @author Thierry Templier
 * @see org.springmodules.lucene.index.core.LuceneIndexTemplate#addDocument(InputStreamDocumentCreator)
 * @see org.springmodules.lucene.index.core.LuceneIndexTemplate#addDocument(InputStreamDocumentCreator, Analyzer)
 */
public interface InputStreamDocumentCreator {

	/**
	 * This method must create the InputStream which will be used
	 * to create a Lucene document to index.
	 * @return the created InputStream
	 * @throws IOException
	 */
	InputStream createInputStream() throws IOException;

	/**
	 * This method must be implemented to specify how to index
	 * a document from the InputStream created by the createInputStream
	 * method.
	 * @param inputStream the InputStream on the resource to index
	 * @return the Lucene Document instance
	 * @throws IOException if thrown by a Lucene method, to be auto-converted
	 * to a LuceneManipulateIndexException
	 */
	Document createDocumentFromInputStream(
								InputStream inputStream) throws Exception;
}
