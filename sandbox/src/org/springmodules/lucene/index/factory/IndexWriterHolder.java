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
import org.apache.lucene.search.Searcher;
import org.springframework.transaction.support.ResourceHolderSupport;

/**
 * @author Thierry Templier
 */
public class IndexWriterHolder extends ResourceHolderSupport {

	private final IndexWriter indexWriter;

	public IndexWriterHolder(IndexWriter indexWriter) {
		this.indexWriter = indexWriter;
	}

	public IndexWriter getIndexWriter() {
		return this.indexWriter;
	}

}
