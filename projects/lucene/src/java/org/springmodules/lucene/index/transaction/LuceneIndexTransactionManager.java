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

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.SimpleAnalyzer;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.transaction.support.AbstractPlatformTransactionManager;
import org.springframework.transaction.support.DefaultTransactionStatus;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springmodules.lucene.index.factory.IndexFactory;
import org.springmodules.lucene.index.factory.IndexReaderFactoryUtils;
import org.springmodules.lucene.index.factory.IndexWriterFactoryUtils;
import org.springmodules.lucene.index.factory.LuceneIndexReader;
import org.springmodules.lucene.index.transaction.cache.CacheTransactionalLuceneIndexReader;
import org.springmodules.lucene.index.transaction.cache.CacheTransactionalLuceneIndexWriter;
import org.springmodules.lucene.index.transaction.compensation.CompensationTransactionalLuceneIndexReader;
import org.springmodules.lucene.index.transaction.compensation.CompensationTransactionalLuceneIndexWriter;

/** 
 * @author Thierry Templier
 */
public class LuceneIndexTransactionManager extends AbstractPlatformTransactionManager implements InitializingBean {
	private IndexFactory indexFactory;
	private Analyzer analyzer;
	private LuceneTransactionProcessor transactionProcessor;
	private boolean optimizeResourcesUsage = false;

	public LuceneIndexTransactionManager() {
	}
	
	public void afterPropertiesSet() throws Exception {
		if( this.transactionProcessor==null ) {
			this.transactionProcessor = new DefaultLuceneTransactionProcessor(this.optimizeResourcesUsage);
		}

		if (this.indexFactory == null) {
			throw new IllegalArgumentException("Property 'indexFactory' is required");
		}
		if (this.analyzer == null) {
			this.analyzer = new SimpleAnalyzer();
		}
	}

	/**
	 * @see AbstractPlatformTransactionManager#doGetTransaction()
	 */
	protected Object doGetTransaction() throws TransactionException {
		LuceneTransactionObject txObject = new LuceneTransactionObject();
		IndexHolder indexHolder =
		    (IndexHolder) TransactionSynchronizationManager.getResource(getIndexFactory());
		txObject.setIndexHolder(indexHolder);
		return txObject;
	}

	/**
	 * @see AbstractPlatformTransactionManager#isExistingTransaction(Object)
	 */
	protected boolean isExistingTransaction(Object transaction) {
		LuceneTransactionObject txObject = (LuceneTransactionObject) transaction;
		// Consider a pre-bound connection as transaction.
		return (txObject.getIndexHolder() != null);
	}

	/**
	 * 
	 * @param definition
	 * @param indexReader
	 * @param transactionalIndexCache
	 * @param rollbackSegment
	 * @return
	 */
	private IndexHolder createTransactionalCacheIndexHolder(TransactionDefinition definition,
										LuceneIndexReader indexReader,
										LuceneTransactionalIndexCache transactionalIndexCache,
										LuceneRollbackSegment rollbackSegment) {
		CacheTransactionalLuceneIndexReader transactionalIndexReader
						= new CacheTransactionalLuceneIndexReader(indexReader, transactionalIndexCache, rollbackSegment);
		CacheTransactionalLuceneIndexWriter transactionalIndexWriter
						= new CacheTransactionalLuceneIndexWriter(transactionalIndexCache, rollbackSegment);

		IndexHolder holder = new IndexHolder(definition, transactionalIndexReader,
							transactionalIndexWriter, transactionalIndexCache, rollbackSegment);
		return holder;
	}

	/**
	 * 
	 * @param definition
	 * @param rollbackSegment
	 * @return
	 */
	private IndexHolder createTransactionalCompensationIndexHolder(TransactionDefinition definition,
										LuceneRollbackSegment rollbackSegment) {
		CompensationTransactionalLuceneIndexReader transactionalIndexReader =
				new CompensationTransactionalLuceneIndexReader(indexFactory, rollbackSegment);
		CompensationTransactionalLuceneIndexWriter transactionalIndexWriter =
				new CompensationTransactionalLuceneIndexWriter(indexFactory, rollbackSegment);

		IndexHolder holder = new IndexHolder(
									definition, transactionalIndexReader,
									transactionalIndexWriter, null, rollbackSegment);
		return holder;
	}

	/**
	 * 
	 * @param txObject
	 * @param definition
	 */
	private void doBeginWithoutCache(LuceneTransactionObject txObject, TransactionDefinition definition) {
		try {
			LuceneRollbackSegment rollbackSegment =
					new DefaultLuceneRollbackSegment(getAnalyzer(), isOptimizeResourcesUsage());
			
			IndexHolder holder = createTransactionalCompensationIndexHolder(definition, rollbackSegment);
			txObject.setIndexHolder(holder);
			txObject.getIndexHolder().setSynchronizedWithTransaction(true);

			//con.getLocalTransaction().begin();
			int timeout = determineTimeout(definition);
			if (timeout != TransactionDefinition.TIMEOUT_DEFAULT) {
				txObject.getIndexHolder().setTimeoutInSeconds(timeout);
			}
			TransactionSynchronizationManager.bindResource(getIndexFactory(), txObject.getIndexHolder());
		}

		catch (Exception ex) {
			throw new TransactionSystemException("Unexpected failure on begin of Lucene transaction", ex);
		}
	}

	/**
	 * 
	 * @param txObject
	 * @param definition
	 */
	private void doBeginWithCache(LuceneTransactionObject txObject, TransactionDefinition definition) {
		LuceneIndexReader indexReader = null;

		try {
			indexReader = getIndexFactory().getIndexReader();

			LuceneTransactionalIndexCache transactionalIndexCache =
					new DefaultLuceneTransactionnalIndexCache();
			transactionalIndexCache.clear();
			LuceneRollbackSegment rollbackSegment =
					new DefaultLuceneRollbackSegment(getAnalyzer(), isOptimizeResourcesUsage());

			if (logger.isDebugEnabled()) {
				logger.debug("Created LuceneTransactionalIndexCache [" + transactionalIndexCache + "] for Lucene transaction");
			}
			
			IndexHolder holder = createTransactionalCacheIndexHolder(definition, indexReader,
														transactionalIndexCache, rollbackSegment);
			txObject.setIndexHolder(holder);
			txObject.getIndexHolder().setSynchronizedWithTransaction(true);

			//con.getLocalTransaction().begin();
			int timeout = determineTimeout(definition);
			if (timeout != TransactionDefinition.TIMEOUT_DEFAULT) {
				txObject.getIndexHolder().setTimeoutInSeconds(timeout);
			}
			TransactionSynchronizationManager.bindResource(getIndexFactory(), txObject.getIndexHolder());
		}

		catch (Exception ex) {
			IndexReaderFactoryUtils.releaseIndexReader(getIndexFactory(), indexReader);
			throw new TransactionSystemException("Unexpected failure on begin of Lucene transaction", ex);
		}
	}

	/**
	 * 
	 * @param txObject
	 */
	private void doBeginReadOnly(LuceneTransactionObject txObject) {
		throw new IllegalArgumentException("Not supported.");
	}

	/**
	 * Determine which transactional strategy uses according to the specified
	 * isolation level.
	 * 
	 * @param definition the transaction definition
	 * @return
	 */
	private boolean isCacheMustBeUsed(TransactionDefinition definition) {
		if( definition.getIsolationLevel()==TransactionDefinition.ISOLATION_READ_UNCOMMITTED ) {
			return false;
		} else if( definition.getIsolationLevel()==TransactionDefinition.ISOLATION_READ_COMMITTED
			|| definition.getIsolationLevel()==TransactionDefinition.ISOLATION_REPEATABLE_READ ) {
			return true;
		} else {
			throw new IllegalArgumentException("The isolation level ISOLATION_SERIALIZABLE is not supported.");
		}
	}
	
	/**
	 * @see AbstractPlatformTransactionManager#doBegin(Object, TransactionDefinition)
	 */
	protected void doBegin(Object transaction, TransactionDefinition definition) {
		LuceneTransactionObject txObject = (LuceneTransactionObject) transaction;

		boolean readOnly = definition.isReadOnly();
		boolean cacheMustBeUsed = isCacheMustBeUsed(definition);
		if( readOnly ) {
			doBeginReadOnly(txObject);
		} else if( !readOnly && !cacheMustBeUsed ) {
			doBeginWithoutCache(txObject, definition);
		} else if( !readOnly && cacheMustBeUsed ) {
			doBeginWithCache(txObject, definition);
		}
	}

	/**
	 * @see AbstractPlatformTransactionManager#doSuspend(Object)
	 */
	protected Object doSuspend(Object transaction) {
		LuceneTransactionObject txObject = (LuceneTransactionObject) transaction;
		txObject.setIndexHolder(null);
		return TransactionSynchronizationManager.unbindResource(getIndexFactory());
	}

	/**
	 * @see AbstractPlatformTransactionManager#doResume(Object, Object)
	 */
	protected void doResume(Object transaction, Object suspendedResources) {
		IndexHolder indexHolder = (IndexHolder) suspendedResources;
		TransactionSynchronizationManager.bindResource(getIndexFactory(), indexHolder);
	}

	/**
	 * @see AbstractPlatformTransactionManager#isRollbackOnly(Object)
	 */
	protected boolean isRollbackOnly(Object transaction) throws TransactionException {
		LuceneTransactionObject txObject = (LuceneTransactionObject) transaction;
		return txObject.getIndexHolder().isRollbackOnly();
	}

	/**
	 * @see AbstractPlatformTransactionManager#doCommit(DefaultTransactionStatus)
	 */
	protected void doCommit(DefaultTransactionStatus status) {
		LuceneTransactionObject txObject = (LuceneTransactionObject) status.getTransaction();
		LuceneTransactionalIndexCache cache = txObject.getIndexHolder().getCache();
		LuceneRollbackSegment rollbackSegment = txObject.getIndexHolder().getRollbackSegment();
		if (status.isDebug()) {
			logger.debug("Committing Lucene transaction on LuceneTransactionalIndexCache [" + cache + "]");
		}
		
		try {
			boolean readOnly = txObject.getIndexHolder().isRollbackOnly();
			boolean cacheMustBeUsed = isCacheMustBeUsed(txObject.getIndexHolder().getTransactionDefintion());
			if( !readOnly && cacheMustBeUsed ) {
				transactionProcessor.applyTransaction(getIndexFactory(), cache, rollbackSegment);
			}
		}
		catch (Exception ex) {
			ex.printStackTrace();
			throw new TransactionSystemException("Unexpected failure on commit of Lucene transaction", ex);
		}
	}

	/**
	 * @see AbstractPlatformTransactionManager#doRollback(DefaultTransactionStatus)
	 */
	protected void doRollback(DefaultTransactionStatus status) {
		LuceneTransactionObject txObject = (LuceneTransactionObject) status.getTransaction();
		LuceneTransactionalIndexCache cache = txObject.getIndexHolder().getCache();
		LuceneRollbackSegment rollbackSegment = txObject.getIndexHolder().getRollbackSegment();
		if (status.isDebug()) {
			logger.debug("Rolling back Lucene transaction on LuceneTransactionalIndexCache [" + cache + "]");
		}
		try {
			boolean cacheMustBeUsed = isCacheMustBeUsed(txObject.getIndexHolder().getTransactionDefintion());
			if( cacheMustBeUsed ) {
				cache.clear();
			} else {
				rollbackSegment.compensate(getIndexFactory());
			}
		}
		catch (Exception ex) {
			throw new TransactionSystemException("Unexpected failure on rollback of Lucene transaction", ex);
		}
	}

	/**
	 * @see AbstractPlatformTransactionManager#doSetRollbackOnly(DefaultTransactionStatus)
	 */
	protected void doSetRollbackOnly(DefaultTransactionStatus status) {
		LuceneTransactionObject txObject = (LuceneTransactionObject) status.getTransaction();
		if (status.isDebug()) {
			logger.debug("Setting Lucene transaction [" + txObject.getIndexHolder().getCache() +
					"] rollback-only");
		}
		txObject.getIndexHolder().setRollbackOnly();
	}

	/**
	 * @see AbstractPlatformTransactionManager#doCleanupAfterCompletion(Object)
	 */
	protected void doCleanupAfterCompletion(Object transaction) {
		LuceneTransactionObject txObject = (LuceneTransactionObject) transaction;

		// Remove the connection holder from the thread.
		TransactionSynchronizationManager.unbindResource(getIndexFactory());
		txObject.getIndexHolder().clear();

		AbstractTransactionalLuceneIndexReader indexReader = txObject.getIndexHolder().getIndexReader();
		AbstractTransactionalLuceneIndexWriter indexWriter = txObject.getIndexHolder().getIndexWriter();
		if (logger.isDebugEnabled()) {
			logger.debug("Releasing Lucene IndexReader [" + indexReader + "] after transaction");
			logger.debug("Releasing Lucene IndexWriter [" + indexWriter + "] after transaction");
		}
		IndexReaderFactoryUtils.closeIndexReader(indexReader);
		IndexWriterFactoryUtils.closeIndexWriter(indexWriter);
	}

	public IndexFactory getIndexFactory() {
		return indexFactory;
	}

	public void setIndexFactory(IndexFactory indexFactory) {
		this.indexFactory = indexFactory;
	}

	public LuceneTransactionProcessor getTransactionProcessor() {
		return transactionProcessor;
	}

	public void setTransactionProcessor(
			LuceneTransactionProcessor transactionProcessor) {
		this.transactionProcessor = transactionProcessor;
	}

	/**
	 * Lucene transaction object, representing a IndexHolder.
	 * Used as transaction object by LuceneIndexTransactionManager.
	 * @see IndexHolder
	 */
	private static class LuceneTransactionObject {

		private IndexHolder indexHolder;

		public void setIndexHolder(IndexHolder indexHolder) {
			this.indexHolder = indexHolder;
		}

		public IndexHolder getIndexHolder() {
			return indexHolder;
		}
	}

	public Analyzer getAnalyzer() {
		return analyzer;
	}

	public void setAnalyzer(Analyzer analyzer) {
		this.analyzer = analyzer;
	}

	public boolean isOptimizeResourcesUsage() {
		return optimizeResourcesUsage;
	}

	public void setOptimizeResourcesUsage(boolean optimizeResourcesUsage) {
		this.optimizeResourcesUsage = optimizeResourcesUsage;
	}

}
