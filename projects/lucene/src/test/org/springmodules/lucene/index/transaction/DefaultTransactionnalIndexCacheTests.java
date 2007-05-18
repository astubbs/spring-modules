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

import java.util.List;

import org.apache.lucene.analysis.SimpleAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.springmodules.lucene.AbstractLuceneTestCase;
import org.springmodules.lucene.index.factory.IndexReaderFactoryUtils;
import org.springmodules.lucene.index.factory.LuceneIndexReader;
import org.springmodules.lucene.index.transaction.cache.CacheTransactionalLuceneIndexReader;
import org.springmodules.lucene.index.transaction.cache.CacheTransactionalLuceneIndexWriter;
import org.springmodules.lucene.index.transaction.operation.LuceneAddOperation;
import org.springmodules.lucene.index.transaction.operation.LuceneDeleteOperation;

/**
 * @author Thierry Templier
 */
public class DefaultTransactionnalIndexCacheTests extends AbstractLuceneTestCase {

	public void testDeleteDocument() throws Exception {
		LuceneIndexReader indexReader = null;
		try {
			indexReader = indexFactory.getIndexReader();
			Document document = indexReader.document(1);

			DefaultLuceneTransactionnalIndexCache cache =
					new DefaultLuceneTransactionnalIndexCache();
			DefaultLuceneRollbackSegment rollbackSegment =
					new DefaultLuceneRollbackSegment(new SimpleAnalyzer(), false);
			CacheTransactionalLuceneIndexReader reader =
					new CacheTransactionalLuceneIndexReader(indexReader, cache, rollbackSegment);

			reader.deleteDocument(1);
			
			List registeredOperations = cache.getOperationsByType(LuceneDeleteOperation.class);
			assertEquals(registeredOperations.size(), 1);
			LuceneDeleteOperation operation = (LuceneDeleteOperation)registeredOperations.get(0);
			assertEquals(operation.getDocuments().length, 1);
			assertTrue(isDocumentsEquals(operation.getDocuments()[0], document));
		} finally {
			IndexReaderFactoryUtils.closeIndexReader(indexReader);
		}
	}

	public void testAddDocument() throws Exception {
		DefaultLuceneTransactionnalIndexCache cache =
				new DefaultLuceneTransactionnalIndexCache();
		DefaultLuceneRollbackSegment rollbackSegment =
				new DefaultLuceneRollbackSegment(new SimpleAnalyzer(), false);
		CacheTransactionalLuceneIndexWriter writer =
				new CacheTransactionalLuceneIndexWriter(cache, rollbackSegment);

		Document document = new Document();
		document.add(new Field("id", "1", Field.Store.YES, Field.Index.UN_TOKENIZED));
		document.add(new Field("field", "a sample", Field.Store.YES, Field.Index.TOKENIZED));
		document.add(new Field("filter", "a sample filter", Field.Store.YES, Field.Index.TOKENIZED));
		document.add(new Field("sort", "2", Field.Store.YES, Field.Index.UN_TOKENIZED));
		writer.addDocument(document);
		
		List registeredOperations = cache.getOperationsByType(LuceneAddOperation.class);
		assertEquals(registeredOperations.size(), 1);
		LuceneAddOperation operation = (LuceneAddOperation)registeredOperations.get(0);
		assertEquals(operation.getDocuments().length, 1);
		assertEquals(operation.getDocuments()[0], document);
	}

	public void testClear() throws Exception {
		LuceneIndexReader indexReader = null;
		try {
			indexReader = indexFactory.getIndexReader();
			DefaultLuceneTransactionnalIndexCache cache =
					new DefaultLuceneTransactionnalIndexCache();
			DefaultLuceneRollbackSegment rollbackSegment =
					new DefaultLuceneRollbackSegment(new SimpleAnalyzer(), false);
			CacheTransactionalLuceneIndexReader reader =
					new CacheTransactionalLuceneIndexReader(indexReader, cache, rollbackSegment);
			CacheTransactionalLuceneIndexWriter writer =
					new CacheTransactionalLuceneIndexWriter(cache, rollbackSegment);

			reader.deleteDocument(1);
			writer.addDocument(new Document());
	
			List registeredOperations = cache.getOperationsByType(LuceneAddOperation.class);
			assertEquals(registeredOperations.size(), 1);
			registeredOperations = cache.getOperationsByType(LuceneDeleteOperation.class);
			assertEquals(registeredOperations.size(), 1);
			
			cache.clear();
	
			registeredOperations = cache.getOperationsByType(LuceneAddOperation.class);
			assertEquals(registeredOperations.size(), 0);
			registeredOperations = cache.getOperationsByType(LuceneDeleteOperation.class);
			assertEquals(registeredOperations.size(), 0);
		} finally {
			IndexReaderFactoryUtils.closeIndexReader(indexReader);
		}
	}
}
