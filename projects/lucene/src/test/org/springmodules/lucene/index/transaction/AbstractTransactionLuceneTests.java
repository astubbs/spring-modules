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

import junit.framework.TestCase;

import org.apache.lucene.analysis.SimpleAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Searcher;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.store.RAMDirectory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springmodules.lucene.index.core.DefaultLuceneIndexTemplate;
import org.springmodules.lucene.index.factory.IndexFactory;
import org.springmodules.lucene.index.factory.SimpleIndexFactory;
import org.springmodules.lucene.search.factory.SearcherFactoryUtils;

/**
 * @author Thierry Templier
 */
public abstract class AbstractTransactionLuceneTests extends TestCase {
	private RAMDirectory directory;
	private IndexFactory indexFactory;

	private void initIndex() {
		System.out.println("---- initIndex -----");
		directory = new RAMDirectory();
		
		IndexWriter writer = null;
		try {
			writer = new IndexWriter(directory, new SimpleAnalyzer(), true);
			initializeIndex(writer);
		} catch(Exception ex) {
			ex.printStackTrace();
		} finally {
			doCloseIndexWriter(writer);
		}
	}

	protected void initializeIndex(IndexWriter writer) throws IOException {
	}

	protected IndexFactory initIndexFactory() {
		System.out.println("---- initIndexFactory -----");
		SimpleIndexFactory indexFactory = new SimpleIndexFactory();
		indexFactory.setDirectory(directory);
		indexFactory.setAnalyzer(new SimpleAnalyzer());
		return indexFactory;
	}
	
	protected final void setUp() throws Exception {
		initIndex();
		this.indexFactory = initIndexFactory();
		doSetUp();
	}

	protected void doSetUp() {
	}

	protected final void tearDown() throws Exception {
		doTearDown();
		if( indexFactory instanceof DisposableBean ) {
			((DisposableBean)indexFactory).destroy();
		}
		indexFactory = null;
		directory.close();
		directory = null;
	}

	protected void doTearDown() {
	}

	protected void doCloseIndexWriter(IndexWriter writer) {
		try {
			if( writer!=null ) {
				writer.close();
			}
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}

	protected DefaultTransactionDefinition createRequiredTransactionDefinition(boolean activateCache) {
		DefaultTransactionDefinition definition = new DefaultTransactionDefinition();
		definition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		if( !activateCache ) {
			definition.setIsolationLevel(TransactionDefinition.ISOLATION_READ_UNCOMMITTED);
		} else {
			definition.setIsolationLevel(TransactionDefinition.ISOLATION_REPEATABLE_READ);
		}
		return definition;
	}

	protected DefaultTransactionDefinition createReadOnlyTransactionDefinition() {
		DefaultTransactionDefinition definition = new DefaultTransactionDefinition();
		definition.setReadOnly(true);
		//definition.
		return definition;
	}

	protected LuceneIndexTransactionManager createTransactionManager(
								boolean optimizeResourcesUsage) throws Exception {
		LuceneIndexTransactionManager transactionManager = new LuceneIndexTransactionManager();
		transactionManager.setIndexFactory(getIndexFactory());
		transactionManager.setAnalyzer(new SimpleAnalyzer());
		transactionManager.setOptimizeResourcesUsage(optimizeResourcesUsage);
		transactionManager.afterPropertiesSet();
		return transactionManager;
	}
	
	protected DefaultLuceneIndexTemplate createLuceneIndexTemplate() {
		DefaultLuceneIndexTemplate template = new DefaultLuceneIndexTemplate();
		template.setIndexFactory(getIndexFactory());
		template.setAnalyzer(new SimpleAnalyzer());
		template.afterPropertiesSet();
		return template;
	}

	protected final boolean isContainedInIndex(Searcher searcher, String key, String value) throws IOException {
		Hits hits = searcher.search(new TermQuery(new Term(key, value)));
		System.out.println("hits ("+key+","+value+"): "+hits.length());
		return (hits.length()==1);
	}
	
	protected final void parseIndex(SearcherCallback callback) {
		Searcher searcher = null; 
		try {
			searcher = new IndexSearcher(directory);
			callback.doInSearcher(searcher);
		} catch(Exception ex) {
			ex.printStackTrace();
		} finally {
			SearcherFactoryUtils.closeSearcher(searcher);
		}
	}

	protected RAMDirectory getDirectory() {
		return directory;
	}

	protected IndexFactory getIndexFactory() {
		return indexFactory;
	}

	protected interface SearcherCallback {
		void doInSearcher(Searcher searcher) throws IOException;
	}

	protected interface IndexWriterCallback {
		void doInWriter(IndexWriter indexWriter) throws IOException;
	}
}
