package org.springmodules.lucene.index.transaction;

import org.apache.lucene.analysis.Analyzer;

public abstract class AbstractLuceneTransactionStrategy implements LuceneTransactionStrategy {
	protected boolean optimizeResourcesUsage;
	protected Analyzer analyzer;

	protected LuceneTransactionalIndexCache getTransactionalIndexCache(LuceneTransactionObject txObject) {
		return txObject.getIndexHolder().getCache();
	}
	
	protected LuceneRollbackSegment getRollbackSegment(LuceneTransactionObject txObject) {
		return txObject.getIndexHolder().getRollbackSegment();
	}

	public void setAnalyzer(Analyzer analyzer) {
		this.analyzer = analyzer;
	}
	
	public void setOptimizeResourcesUsage(boolean optimizeResourcesUsage) {
		this.optimizeResourcesUsage = optimizeResourcesUsage;
	}
}
