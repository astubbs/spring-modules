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
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.springmodules.lucene.search.factory.LuceneHits;

/**
 * Callback interface for update Lucene document instances. In fact,
 * with Lucene, you can't update documents of the index. So you need
 * to remove them and insert new ones with the modified fields.
 * This callback helps you to do that by providing the old documents
 * in order to create new ones which will be inserted in the index.
 *
 * <p>Used for input Document in the updateDocuments methods in
 * LuceneIndexTemplate.
 *
 * @author Thierry Templier
 * @see LuceneIndexTemplate#updateDocuments(DocumentsModifier, DocumentsIdentifier)
 * @see LuceneIndexTemplate#updateDocuments(DocumentsModifier, DocumentsIdentifier, Analyzer)
 */
public interface DocumentsModifier {

	/**
	 * Create Lucene document instances basing existing ones. These
	 * latter will be delete from the index in order to be replaced by
	 * the created ones.
	 * @param document the Lucene Document to update 
	 * @return the new Lucene Document instance based on the old
	 * @throws IOException if thrown by a Lucene method, to be auto-converted
	 * to a LuceneManipulateIndexException
	 */
	List updateDocuments(LuceneHits hits) throws IOException;
}
