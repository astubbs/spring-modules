package org.springmodules.lucene.index.transaction;

/**
 * Lucene transaction object, representing a IndexHolder.
 * Used as transaction object by LuceneIndexTransactionManager.
 * @see IndexHolder
 */
public class LuceneTransactionObject {

	private IndexHolder indexHolder;

	public void setIndexHolder(IndexHolder indexHolder) {
		this.indexHolder = indexHolder;
	}

	public IndexHolder getIndexHolder() {
		return indexHolder;
	}
}

