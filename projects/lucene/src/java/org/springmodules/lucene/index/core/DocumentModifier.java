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

import org.apache.lucene.document.Document;

/**
 * Callback interface for update a Lucene document instance. In fact,
 * with Lucene, you can't update a document of the index. So you need
 * to remove it and insert a new one with the modified fields.
 * This callback helps you to do that by providing the old document
 * in order to create a new one which will be inserted in the index.
 *
 * <p>Used for input Document in the updateDocument methods in
 * LuceneIndexTemplate.
 *
 * @author Thierry Templier
 * @see LuceneIndexTemplate#updateDocument(DocumentModifier, DocumentIdentifier)
 * @see LuceneIndexTemplate#updateDocument(DocumentModifier, DocumentIdentifier, Analyzer)
 */
public interface DocumentModifier {

	/**
	 * Create a Lucene document instance basing an existing one. This
	 * latter will be delete from the index in order to be replaced by
	 * the created one.
	 * @param document the Lucene Document to update 
	 * @return the new Lucene Document instance based on the old
	 * @throws IOException if thrown by a Lucene method, to be auto-converted
	 * to a LuceneManipulateIndexException
	 */
	Document updateDocument(Document document) throws Exception;
}
