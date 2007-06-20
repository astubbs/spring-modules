package org.springmodules.lucene.index.transaction.cache;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.lucene.analysis.SimpleAnalyzer;
import org.springframework.transaction.TransactionDefinition;
import org.springmodules.lucene.index.factory.IndexFactory;
import org.springmodules.lucene.index.transaction.AbstractLuceneTransactionStrategy;
import org.springmodules.lucene.index.transaction.DefaultLuceneRollbackSegment;
import org.springmodules.lucene.index.transaction.DefaultLuceneTransactionProcessor;
import org.springmodules.lucene.index.transaction.DefaultLuceneTransactionnalIndexCache;
import org.springmodules.lucene.index.transaction.IndexHolder;
import org.springmodules.lucene.index.transaction.LuceneRollbackSegment;
import org.springmodules.lucene.index.transaction.LuceneTransactionObject;
import org.springmodules.lucene.index.transaction.LuceneTransactionProcessor;
import org.springmodules.lucene.index.transaction.LuceneTransactionalIndexCache;

public class LuceneCacheTransactionStrategy extends AbstractLuceneTransactionStrategy {

	protected final Log logger = LogFactory.getLog(getClass());

	/**
	 * 
	 * @param definition
	 * @param indexReader
	 * @param transactionalIndexCache
	 * @param rollbackSegment
	 * @return
	 */
	protected IndexHolder createTransactionalCacheIndexHolder(TransactionDefinition definition,
										IndexFactory indexFactory,
										LuceneTransactionalIndexCache transactionalIndexCache,
										LuceneRollbackSegment rollbackSegment) {
		CacheTransactionalLuceneIndexReader transactionalIndexReader
				= new CacheTransactionalLuceneIndexReader(indexFactory, transactionalIndexCache, rollbackSegment);
		CacheTransactionalLuceneIndexWriter transactionalIndexWriter
				= new CacheTransactionalLuceneIndexWriter(transactionalIndexCache, rollbackSegment);

		IndexHolder holder = new IndexHolder(definition, transactionalIndexReader,
							transactionalIndexWriter, transactionalIndexCache, rollbackSegment);
		return holder;
	}

	public IndexHolder doBegin(IndexFactory indexFactory, LuceneTransactionObject txObject,
								TransactionDefinition definition) throws Exception {
		LuceneTransactionalIndexCache transactionalIndexCache =
			new DefaultLuceneTransactionnalIndexCache();
		transactionalIndexCache.clear();

		LuceneRollbackSegment rollbackSegment =
			new DefaultLuceneRollbackSegment(this.analyzer, this.optimizeResourcesUsage);

		if (logger.isDebugEnabled()) {
			logger.debug("Created LuceneTransactionalIndexCache ["
					+ transactionalIndexCache + "] for Lucene transaction");
		}
	
		return createTransactionalCacheIndexHolder(
						definition, indexFactory, transactionalIndexCache, rollbackSegment);
	}

	public void doCommit(IndexFactory indexFactory, LuceneTransactionObject txObject) throws Exception {
		LuceneTransactionalIndexCache cache = getTransactionalIndexCache(txObject);
		LuceneRollbackSegment rollbackSegment = getRollbackSegment(txObject);

		LuceneTransactionProcessor processor = new DefaultLuceneTransactionProcessor(
															this.optimizeResourcesUsage);
		processor.applyTransaction(indexFactory, cache, rollbackSegment);
	}

	public void doRollback(IndexFactory indexFactory, LuceneTransactionObject txObject) throws Exception {
		LuceneTransactionalIndexCache cache = getTransactionalIndexCache(txObject);
		cache.clear();
	}

}
