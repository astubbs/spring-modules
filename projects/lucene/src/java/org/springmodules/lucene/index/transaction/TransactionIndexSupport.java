package org.springmodules.lucene.index.transaction;

import org.springmodules.lucene.index.core.ReaderCallback;
import org.springmodules.lucene.index.core.WriterCallback;
import org.springmodules.lucene.index.factory.IndexFactory;
import org.springmodules.lucene.index.factory.IndexReaderFactoryUtils;
import org.springmodules.lucene.index.factory.IndexWriterFactoryUtils;
import org.springmodules.lucene.index.factory.LuceneIndexReader;
import org.springmodules.lucene.index.factory.LuceneIndexWriter;

public abstract class TransactionIndexSupport {
	private IndexFactory delegate;
	
	protected final Object executeOnReader(ReaderCallback callback) {
		LuceneIndexReader indexReader = null;
		try {
			indexReader = IndexReaderFactoryUtils.getIndexReader(delegate, false);
			return callback.doWithReader(indexReader);
		} catch(Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			IndexReaderFactoryUtils.releaseIndexReader(delegate, indexReader, false);
		}
	}
	
	protected Object executeOnWriter(WriterCallback callback) {
		LuceneIndexWriter indexWriter = null;
		try {
			indexWriter = IndexWriterFactoryUtils.getIndexWriter(delegate, false);
			return callback.doWithWriter(indexWriter);
		} catch(Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			IndexWriterFactoryUtils.releaseIndexWriter(delegate, indexWriter, false);
		}
	}
	
	public IndexFactory getDelegate() {
		return delegate;
	}

	public void setDelegate(IndexFactory delegate) {
		this.delegate = delegate;
	}

}
