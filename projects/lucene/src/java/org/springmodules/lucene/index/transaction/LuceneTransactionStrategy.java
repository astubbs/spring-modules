package org.springmodules.lucene.index.transaction;

import org.apache.lucene.analysis.Analyzer;
import org.springframework.transaction.TransactionDefinition;
import org.springmodules.lucene.index.factory.IndexFactory;

public interface LuceneTransactionStrategy {
	void setAnalyzer(Analyzer analyzer);
	void setOptimizeResourcesUsage(boolean optimizeResourcesUsage);
	IndexHolder doBegin(IndexFactory indexFactory,
			LuceneTransactionObject txObject, TransactionDefinition definition) throws Exception;
	void doCommit(IndexFactory indexFactory, LuceneTransactionObject txObject) throws Exception;
	void doRollback(IndexFactory indexFactory, LuceneTransactionObject txObject) throws Exception;
}
