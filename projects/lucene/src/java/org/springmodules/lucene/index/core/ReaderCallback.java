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

import org.springmodules.lucene.index.factory.LuceneIndexReader;

/**
 * Generic callback interface for code that operates on a Lucene IndexReader.
 * Allows to execute any number of operations on a single IndexReader.
 * 
 * <p>This is particularly useful for delegating to existing data access code
 * that expects an IndexReader to work on and throws IOException. For newly
 * written code, it is strongly recommended to use LuceneIndexTemplate's more
 * specific methods.
 *
 * @author Brian McCallister
 * @author Thierry Templier
 * @see org.springmodules.lucene.index.core.LuceneIndexTemplate
 * @see LuceneIndexReader
 */
public interface ReaderCallback {

    /**
	 * Gets called by <code>LuceneIndexTemplate.read</code> with an active Lucene
	 * IndexReader. Does not need to care about activating or closing the IndexReader.
	 * 
	 * <p>Allows for returning a result object created within the callback, i.e.
	 * a domain object or a collection of domain objects. A thrown
	 * RuntimeException is treated as application exception: it gets propagated
	 * to the caller of the template.
	 * 
	 * @param reader an LuceneIndexReader instance
	 * @return a result object, or null if none
	 * @throws IOException if thrown by a Lucene method, to be auto-converted
	 * to a LuceneIndexAccessException
	 */
	Object doWithReader(LuceneIndexReader reader) throws IOException;
}
