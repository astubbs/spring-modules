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

/**
 * Callback interface for creating a list of Lucene document instances
 * at the same time.
 *
 * <p>Used for input Document creation in LuceneIndexTemplate. Alternatively,
 * Document instances can be passed into LuceneIndexTemplate's corresponding
 * <code>addDocuments</code> methods directly.
 *
 * @author Thierry Templier
 * @see LuceneIndexTemplate#addDocuments(DocumentsCreator)
 * @see LuceneIndexTemplate#addDocuments(DocumentsCreator,org.apache.lucene.analysis.Analyzer)
 */
public interface DocumentsCreator {

	/**
	 * Create a list of Lucene document instances.
	 * <p>For use as <i>input</i> creator with LuceneIndexTemplate's
	 * <code>addDocuments</code> methods,
	 * this method should create a list of <i>populated</i> Document instances.
	 * @return the Document instance
	 * @throws IOException if thrown by a Lucene method, to be auto-converted
	 * to a LuceneManipulateIndexException
	 */
	List createDocuments() throws Exception;
}
