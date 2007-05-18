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

package org.springmodules.lucene.index.transaction;

import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.ResourceHolderSupport;
import org.springmodules.lucene.index.factory.IndexReaderFactoryUtils;
import org.springmodules.lucene.index.factory.IndexWriterFactoryUtils;

/**
 * Index holder, wrapping LuceneIndexReader and LuceneIndexWriter.
 *
 * <p>LuceneIndexTransactionManager binds instances of this class
 * to the thread, for a given IndexFactory.
 *
 * <p>Note: This is an SPI class, not intended to be used by applications.
 *
 * @author Thierry Templier
 * @see LuceneIndexTransactionManager
 * @see IndexReaderFactoryUtils
 * @see IndexWriterFactoryUtils
 */
public class IndexHolder extends ResourceHolderSupport {

	private final TransactionDefinition definition;
	private final AbstractTransactionalLuceneIndexReader indexReader;
	private final AbstractTransactionalLuceneIndexWriter indexWriter;
	private final LuceneTransactionalIndexCache cache;
	private final LuceneRollbackSegment rollbackSegment;

	public IndexHolder(TransactionDefinition definition,
					AbstractTransactionalLuceneIndexReader indexReader,
					AbstractTransactionalLuceneIndexWriter indexWriter,
					LuceneTransactionalIndexCache cache,
					LuceneRollbackSegment rollbackSegment) {
		this.definition = definition;
		this.indexReader = indexReader;
		this.indexWriter = indexWriter;
		this.cache = cache;
		this.rollbackSegment = rollbackSegment;
	}

	public TransactionDefinition getTransactionDefintion() {
		return definition;
	}

	public AbstractTransactionalLuceneIndexReader getIndexReader() {
		return this.indexReader;
	}

	public AbstractTransactionalLuceneIndexWriter getIndexWriter() {
		return this.indexWriter;
	}

	public LuceneTransactionalIndexCache getCache() {
		return this.cache;
	}

	public LuceneRollbackSegment getRollbackSegment() {
		return rollbackSegment;
	}

}
