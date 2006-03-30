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

import org.apache.lucene.index.Term;

/**
 * Callback interface for identify the document to update.
 *
 * <p>Used for input Document in the updateDocument methods in
 * LuceneIndexTemplate.
 *
 * @author Thierry Templier
 * @see LuceneIndexTemplate#updateDocument(DocumentModifier, DocumentIdentifier)
 * @see LuceneIndexTemplate#updateDocument(DocumentModifier, DocumentIdentifier, Analyzer)
 */
public interface DocumentIdentifier {
	
	/**
	 * Create a Lucene term which identifies the document to update.
	 * @return the term which identifies the document to update
	 */
	Term getIdentifier();
}
