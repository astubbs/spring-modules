package org.springmodules.lucene.index.factory.concurrent;

import junit.framework.TestCase;

import org.apache.lucene.analysis.SimpleAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.store.RAMDirectory;
import org.springmodules.lucene.index.factory.IndexFactory;
import org.springmodules.lucene.index.factory.LuceneIndexReader;
import org.springmodules.lucene.index.factory.LuceneIndexWriter;
import org.springmodules.lucene.index.factory.SimpleIndexFactory;

public class LockIndexFactoryTests extends TestCase {

	private void doCloseIndexWriter(LuceneIndexWriter indexWriter) {
		try {
			if( indexWriter!=null ) {
				indexWriter.close();
			}
		} catch(Exception ex) {}
	}
	
	protected void initIndex(IndexFactory indexFactory) {
		LuceneIndexWriter indexWriter = null;
		try {
			Document document = new Document();
			document.add(new Field("id", "1", Field.Store.YES, Field.Index.UN_TOKENIZED));
			document.add(new Field("field", "a sample", Field.Store.YES, Field.Index.TOKENIZED));
			document.add(new Field("filter", "a sample filter", Field.Store.YES, Field.Index.TOKENIZED));
			document.add(new Field("sort", "2", Field.Store.YES, Field.Index.UN_TOKENIZED));
			indexWriter.addDocument(document);
		} catch(Exception ex) {
		} finally {
			doCloseIndexWriter(indexWriter);
		}
	}

	public void testGetResourceFactory() throws Exception {
		RAMDirectory directory = new RAMDirectory();
		SimpleIndexFactory targetIndexFactory = new SimpleIndexFactory();
		targetIndexFactory.setDirectory(directory);
		targetIndexFactory.setCreate(true);
		initIndex(targetIndexFactory);
		
		LockIndexFactory indexFactory = null;
		try {
			indexFactory = new LockIndexFactory();
			indexFactory.setTargetIndexFactory(targetIndexFactory);
			indexFactory.afterPropertiesSet();
		
			LuceneIndexReader indexReader = indexFactory.getIndexReader();
			indexReader.hasDeletions();
			indexReader.close();
		
			LuceneIndexWriter indexWriter = indexFactory.getIndexWriter();
			Document document = new Document();
			document.add(new Field("id", "1", Field.Store.YES, Field.Index.UN_TOKENIZED));
			document.add(new Field("field", "a sample", Field.Store.YES, Field.Index.TOKENIZED));
			document.add(new Field("filter", "a sample filter", Field.Store.YES, Field.Index.TOKENIZED));
			document.add(new Field("sort", "2", Field.Store.YES, Field.Index.UN_TOKENIZED));
			indexWriter.addDocument(document, new SimpleAnalyzer());
			indexWriter.close();
		} catch(Exception ex) {
			ex.printStackTrace();
		} finally {
			if( indexFactory!=null ) {
				indexFactory.destroy();
			}
		}
	}
}
