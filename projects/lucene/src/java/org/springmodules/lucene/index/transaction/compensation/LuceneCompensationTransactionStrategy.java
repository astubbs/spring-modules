package org.springmodules.lucene.index.transaction.compensation;

import org.apache.lucene.analysis.SimpleAnalyzer;
import org.springframework.transaction.TransactionDefinition;
import org.springmodules.lucene.index.factory.IndexFactory;
import org.springmodules.lucene.index.transaction.AbstractLuceneTransactionStrategy;
import org.springmodules.lucene.index.transaction.DefaultLuceneRollbackSegment;
import org.springmodules.lucene.index.transaction.IndexHolder;
import org.springmodules.lucene.index.transaction.LuceneRollbackSegment;
import org.springmodules.lucene.index.transaction.LuceneTransactionObject;

public class LuceneCompensationTransactionStrategy extends AbstractLuceneTransactionStrategy {

	/**
	 * 
	 * @param definition
	 * @param rollbackSegment
	 * @return
	 */
	protected IndexHolder createTransactionalCompensationIndexHolder(IndexFactory indexFactory,
						TransactionDefinition definition, LuceneRollbackSegment rollbackSegment) {
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
	public IndexHolder doBegin(IndexFactory indexFactory, LuceneTransactionObject txObject,
						TransactionDefinition definition) throws Exception {
		//TODO: manage the creation of analyzer and the activation of the optimize resource
		/*LuceneRollbackSegment rollbackSegment =
				new DefaultLuceneRollbackSegment(getAnalyzer(), isOptimizeResourcesUsage());*/
		LuceneRollbackSegment rollbackSegment =
				new DefaultLuceneRollbackSegment(new SimpleAnalyzer(), false);
		
		return createTransactionalCompensationIndexHolder(
									indexFactory, definition, rollbackSegment);
	}

	public void doCommit(IndexFactory indexFactory, LuceneTransactionObject txObject) throws Exception {
	}

	public void doRollback(IndexFactory indexFactory, LuceneTransactionObject txObject) throws Exception {
		LuceneRollbackSegment rollbackSegment = getRollbackSegment(txObject);		
		rollbackSegment.compensate(indexFactory);
	}

}
