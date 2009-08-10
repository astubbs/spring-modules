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

package org.springmodules.lucene.index.resource;

import org.springmodules.lucene.index.LuceneIndexingException;
import org.springmodules.lucene.index.factory.LuceneIndexReader;
import org.springmodules.lucene.index.factory.LuceneIndexWriter;

/**
 * @author Thierry Templier
 */
public class ResourceHolder {
	private LuceneIndexReader indexReader;
	private LuceneIndexWriter indexWriter;
	
	public ResourceHolder(LuceneIndexReader indexReader, LuceneIndexWriter indexWriter) {
		this.indexReader = indexReader;
		this.indexWriter = indexWriter;
	}

	public LuceneIndexReader getIndexReader() {
		return getIndexReader(true);
	}

	public LuceneIndexReader getIndexReader(boolean throwException) {
		if( indexReader==null && throwException) {
			throw new LuceneIndexingException("You can not used an IndexReader in this context.");
		}
		return indexReader;
	}

	public LuceneIndexWriter getIndexWriter() {
		return getIndexWriter(true);
	}

	public LuceneIndexWriter getIndexWriter(boolean throwException) {
		if( indexWriter==null && throwException ) {
			throw new LuceneIndexingException("You can not used an IndexWriter in this context.");
		}
		return indexWriter;
	}

}
