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
 * Index holder, wrapping both Lucene IndexReader and IndexWriter.
 *
 * <p>LuceneIndexResourceManager binds instances of this class
 * to the thread, for a given IndexFactory.
 *
 * <p>Note: This is an SPI class, not intended to be used by applications.
 *
 * @author Thierry Templier
 */
public class IndexHolder {

	private IndexReader indexReader;
	private IndexWriter indexWriter;

	public IndexHolder(IndexReader indexReader,IndexWriter indexWriter) {
		this.indexReader = indexReader;
		this.indexWriter = indexWriter;
	}

	public IndexReader getIndexReader() {
		return this.indexReader;
	}

	public IndexWriter getIndexWriter() {
		return this.indexWriter;
	}

	public void setIndexReader(IndexReader indexReader) {
		if( this.indexReader==null ) {
			this.indexReader=indexReader;
		}
	}

	public void setIndexWriter(IndexWriter indexWriter) {
		if( this.indexWriter==null ) {
			this.indexWriter=indexWriter;
		}
	}
}
