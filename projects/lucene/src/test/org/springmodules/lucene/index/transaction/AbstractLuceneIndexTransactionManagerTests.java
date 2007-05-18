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

import java.io.IOException;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.Searcher;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springmodules.lucene.index.core.DefaultLuceneIndexTemplate;

/**
 * @author Thierry Templier
 */
public abstract class AbstractLuceneIndexTransactionManagerTests extends AbstractTransactionLuceneTests {

	protected void initializeIndex(IndexWriter writer) throws IOException {
		Document document1 = new Document();
		document1.add(new Field("id", "1", Field.Store.YES, Field.Index.UN_TOKENIZED));
		document1.add(new Field("field", "a sample", Field.Store.YES, Field.Index.TOKENIZED));
		document1.add(new Field("filter", "a sample filter", Field.Store.YES, Field.Index.TOKENIZED));
		document1.add(new Field("sort", "2", Field.Store.YES, Field.Index.UN_TOKENIZED));
		writer.addDocument(document1);

		Document document2 = new Document();
		document2.add(new Field("id", "2", Field.Store.YES, Field.Index.UN_TOKENIZED));
		document2.add(new Field("field", "a sample", Field.Store.YES, Field.Index.TOKENIZED));
		document2.add(new Field("filter", "a sample filter", Field.Store.YES, Field.Index.TOKENIZED));
		document2.add(new Field("test", "value", Field.Store.YES, Field.Index.TOKENIZED));
		document2.add(new Field("sort", "2", Field.Store.YES, Field.Index.UN_TOKENIZED));
		writer.addDocument(document2);
	}
	
	protected abstract boolean getActivateCacheTransactionManager();

	protected abstract boolean getOptimizeResourcesUsage();

	private void checkCommitedIndex() {
		parseIndex(new SearcherCallback() {
			public void doInSearcher(Searcher searcher) throws IOException {
				assertTrue(isContainedInIndex(searcher, "id", "1"));
				assertFalse(isContainedInIndex(searcher, "id", "2"));
				assertTrue(isContainedInIndex(searcher, "id", "3"));
			}
		});
	}

	public void testTransactionCommit() throws Exception {
		DefaultTransactionDefinition definition =
				createRequiredTransactionDefinition(getActivateCacheTransactionManager());
		LuceneIndexTransactionManager transactionManager =
				createTransactionManager(getOptimizeResourcesUsage());
		
		TransactionStatus status = transactionManager.getTransaction(definition);
		
		DefaultLuceneIndexTemplate template = createLuceneIndexTemplate();
		
		//Adding a document
		Document document = new Document();
		document.add(new Field("id", "3", Field.Store.YES, Field.Index.UN_TOKENIZED));
		template.addDocument(document);
		
		//Deleting documents
		Term term = new Term("test", "value");
		template.deleteDocuments(term);
		
		transactionManager.commit(status);
		
		//Check the index
		checkCommitedIndex();
	}
	
	private void checkRollbackedIndex() {
		parseIndex(new SearcherCallback() {
			public void doInSearcher(Searcher searcher) throws IOException {
				assertTrue(isContainedInIndex(searcher, "id", "1"));
				assertTrue(isContainedInIndex(searcher, "id", "2"));
				assertFalse(isContainedInIndex(searcher, "id", "3"));
			}
		});
	}

	public void testTransactionRollback() throws Exception {
		DefaultTransactionDefinition definition =
				createRequiredTransactionDefinition(getActivateCacheTransactionManager());
		LuceneIndexTransactionManager transactionManager =
				createTransactionManager(getOptimizeResourcesUsage());
		
		TransactionStatus status = transactionManager.getTransaction(definition);
		
		DefaultLuceneIndexTemplate template = createLuceneIndexTemplate();
		
		//Adding a document
		Document document = new Document();
		document.add(new Field("id", "3", Field.Store.YES, Field.Index.UN_TOKENIZED));
		template.addDocument(document);
		
		//Deleting documents
		Term term = new Term("test", "value");
		template.deleteDocuments(term);
		
		transactionManager.rollback(status);

		//Check the index
		checkRollbackedIndex();
	}
	
	public void testTransactionSetRollbackOnly() throws Exception {
		DefaultTransactionDefinition definition =
				createRequiredTransactionDefinition(getActivateCacheTransactionManager());
		LuceneIndexTransactionManager transactionManager =
				createTransactionManager(getOptimizeResourcesUsage());
		
		TransactionStatus status = transactionManager.getTransaction(definition);
		
		DefaultLuceneIndexTemplate template = createLuceneIndexTemplate();
		
		status.setRollbackOnly();
		
		//Adding a document
		Document document = new Document();
		document.add(new Field("id", "3", Field.Store.YES, Field.Index.UN_TOKENIZED));
		template.addDocument(document);
		
		//Deleting documents
		Term term = new Term("test", "value");
		template.deleteDocuments(term);
		
		transactionManager.rollback(status);

		//Check the index
		checkRollbackedIndex();
	}
	
	private void checkCommitedIndexWithUndeleteAll() {
		parseIndex(new SearcherCallback() {
			public void doInSearcher(Searcher searcher) throws IOException {
				assertTrue(isContainedInIndex(searcher, "id", "1"));
				assertTrue(isContainedInIndex(searcher, "id", "2"));
				assertTrue(isContainedInIndex(searcher, "id", "3"));
			}
		});
	}

	public void testTransactionCommitHasDeletion() throws Exception {
		DefaultTransactionDefinition definition =
				createRequiredTransactionDefinition(getActivateCacheTransactionManager());
		LuceneIndexTransactionManager transactionManager =
				createTransactionManager(getOptimizeResourcesUsage());
		
		TransactionStatus status = transactionManager.getTransaction(definition);
		
		DefaultLuceneIndexTemplate template = createLuceneIndexTemplate();
		
		//Adding a document
		Document document = new Document();
		document.add(new Field("id", "3", Field.Store.YES, Field.Index.UN_TOKENIZED));
		template.addDocument(document);
		
		//Deleting documents
		Term term = new Term("test", "value");
		template.deleteDocuments(term);
		
		//HasDeletions
		assertTrue(template.hasDeletions());
		
		transactionManager.commit(status);
		
		//Check the index
		checkCommitedIndex();
	}

	public void testTransactionCommitUndeleteAll() throws Exception {
		DefaultTransactionDefinition definition =
				createRequiredTransactionDefinition(getActivateCacheTransactionManager());
		LuceneIndexTransactionManager transactionManager =
				createTransactionManager(getOptimizeResourcesUsage());
		
		TransactionStatus status = transactionManager.getTransaction(definition);
		
		DefaultLuceneIndexTemplate template = createLuceneIndexTemplate();
		
		//Adding a document
		Document document = new Document();
		document.add(new Field("id", "3", Field.Store.YES, Field.Index.UN_TOKENIZED));
		template.addDocument(document);
		
		//Deleting documents
		Term term = new Term("test", "value");
		template.deleteDocuments(term);
		
		//UndeleteAll
		template.undeleteDocuments();
		
		//HasDeletions
		assertFalse(template.hasDeletions());
		
		transactionManager.commit(status);
		
		//Check the index
		checkCommitedIndexWithUndeleteAll();
	}

}
