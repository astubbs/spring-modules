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

package org.springmodules.lucene.index.factory;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;

/**
 * <p>This is the index factory abstraction to get reader and writer
 * instances to work on a Lucene index. These instances can be used
 * to get informations about it or manipulate it with the LuceneIndexTemplate
 * class or with the different indexer classes.
 * 
 * @author Thierry Templier
 * @see org.apache.lucene.index.IndexReader
 * @see org.apache.lucene.index.IndexWriter
 * @see org.springmodules.lucene.index.core.LuceneIndexTemplate
 * @see org.springmodules.lucene.index.object.directory.DirectoryIndexer
 * @see org.springmodules.lucene.index.object.database.DatabaseIndexer
 */
public interface IndexFactory {

	/**
	 * Contruct an IndexReader instance. This instance will be used by the
	 * LuceneIndexTemplate to get informations about the index and make delete
	 * operations on the index. 
	 * @return the IndexReader instance
	 */
	IndexReader getIndexReader();

	/**
	 * Contruct an IndexWriter instance. This instance will be used by both
	 * the LuceneIndexTemplate and every indexers to add documents and
	 * optimize it.
	 * @return the IndexWriter instance
	 */
	IndexWriter getIndexWriter();
}
