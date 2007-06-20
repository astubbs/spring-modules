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

import java.util.HashMap;
import java.util.Map;

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
import org.springmodules.lucene.index.transaction.cache.LuceneCacheTransactionStrategy;
import org.springmodules.lucene.index.transaction.compensation.LuceneCompensationTransactionStrategy;

/** 
 * @author Thierry Templier
 */
public class LuceneIndexTransactionManager extends AbstractPlatformTransactionManager implements InitializingBean {
	private IndexFactory indexFactory;
	private Analyzer analyzer;
	private LuceneTransactionProcessor transactionProcessor;
	private boolean optimizeResourcesUsage = false;
	Map transactionStrategies = new HashMap();

	//protected final Log logger = LogFactory.getLog(getClass());

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
		
		LuceneCacheTransactionStrategy cacheStrategy = new LuceneCacheTransactionStrategy();
		cacheStrategy.setAnalyzer(analyzer);
		cacheStrategy.setOptimizeResourcesUsage(optimizeResourcesUsage);
		transactionStrategies.put(
				new Integer(TransactionDefinition.ISOLATION_REPEATABLE_READ), cacheStrategy);

		LuceneCacheTransactionStrategy compensationStrategy = new LuceneCacheTransactionStrategy();
		compensationStrategy.setAnalyzer(analyzer);
		compensationStrategy.setOptimizeResourcesUsage(optimizeResourcesUsage);
		transactionStrategies.put(
				new Integer(TransactionDefinition.ISOLATION_READ_UNCOMMITTED), compensationStrategy);
	}

	private LuceneTransactionStrategy getTransactionStrategy(int isolationLevel) {
		LuceneTransactionStrategy strategy
			= (LuceneTransactionStrategy)transactionStrategies.get(new Integer(isolationLevel));
		
		if( strategy==null ) {
			throw new LuceneTransactionException("No strategy supported to the isolation level "+isolationLevel);
		}
		
		return strategy;
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
		if( logger.isDebugEnabled() ) {
			logger.debug(cacheMustBeUsed ? "Cache mode for transaction activated"
									: "Cache mode for transaction not activated");
			logger.debug(readOnly ? "Read only mode for transaction activated"
					: "Read only mode for transaction not activated");
		}
		
		try {
			/*if( readOnly ) {
				doBeginReadOnly(txObject);
			} else if( !readOnly && !cacheMustBeUsed ) {
				doBeginWithoutCache(txObject, definition);
			} else if( !readOnly && cacheMustBeUsed ) {
				doBeginWithCache(txObject, definition);
			}*/
			
			LuceneTransactionStrategy strategy = getTransactionStrategy(definition.getIsolationLevel());
			IndexHolder holder = strategy.doBegin(getIndexFactory(), txObject, definition);

			txObject.setIndexHolder(holder);
			txObject.getIndexHolder().setSynchronizedWithTransaction(true);

			int timeout = determineTimeout(definition);
			if (timeout != TransactionDefinition.TIMEOUT_DEFAULT) {
				txObject.getIndexHolder().setTimeoutInSeconds(timeout);
			}
			TransactionSynchronizationManager.bindResource(indexFactory, txObject.getIndexHolder());
		} catch (Exception ex) {
			throw new TransactionSystemException("Unexpected failure on begin of Lucene transaction", ex);
		}
	}

	/*protected abstract void doBeginWithoutCache(
				LuceneTransactionObject txObject, TransactionDefinition definition) throws Exception;

	protected abstract void doBeginWithCache(
				LuceneTransactionObject txObject, TransactionDefinition definition) throws Exception;*/

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
		TransactionDefinition definition = txObject.getIndexHolder().getTransactionDefinition();
		/*LuceneTransactionalIndexCache cache = txObject.getIndexHolder().getCache();
		LuceneRollbackSegment rollbackSegment = txObject.getIndexHolder().getRollbackSegment();
		if (status.isDebug()) {
			logger.debug("Committing Lucene transaction on LuceneTransactionalIndexCache [" + cache + "]");
		}*/
		
		try {
			/*boolean readOnly = txObject.getIndexHolder().isRollbackOnly();
			boolean cacheMustBeUsed = isCacheMustBeUsed(txObject.getIndexHolder().getTransactionDefinition());
			if( !readOnly && cacheMustBeUsed ) {
				doCommitWithCache(cache, rollbackSegment);
			}*/
			LuceneTransactionStrategy strategy = getTransactionStrategy(definition.getIsolationLevel());
			strategy.doCommit(getIndexFactory(), txObject);
		}
		catch (Exception ex) {
			ex.printStackTrace();
			throw new TransactionSystemException("Unexpected failure on commit of Lucene transaction", ex);
		}
	}

	/*protected abstract void doCommitWithCache(
				LuceneTransactionalIndexCache cache, LuceneRollbackSegment rollbackSegment);*/

	/**
	 * @see AbstractPlatformTransactionManager#doRollback(DefaultTransactionStatus)
	 */
	protected void doRollback(DefaultTransactionStatus status) {
		LuceneTransactionObject txObject = (LuceneTransactionObject) status.getTransaction();
		TransactionDefinition definition = txObject.getIndexHolder().getTransactionDefinition();
		/*LuceneTransactionalIndexCache cache = txObject.getIndexHolder().getCache();
		LuceneRollbackSegment rollbackSegment = txObject.getIndexHolder().getRollbackSegment();
		if (status.isDebug()) {
			logger.debug("Rolling back Lucene transaction on LuceneTransactionalIndexCache [" + cache + "]");
		}*/
		
		try {
			/*boolean cacheMustBeUsed = isCacheMustBeUsed(txObject.getIndexHolder().getTransactionDefinition());
			if( cacheMustBeUsed ) {
				doRollbackWithCache(cache);
			} else {
				doRollbackWithoutCache(rollbackSegment);
			}*/
			LuceneTransactionStrategy strategy = getTransactionStrategy(definition.getIsolationLevel());
			strategy.doRollback(getIndexFactory(), txObject);
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
