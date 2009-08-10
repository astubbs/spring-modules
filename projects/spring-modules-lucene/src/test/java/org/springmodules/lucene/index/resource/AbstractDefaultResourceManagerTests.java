package org.springmodules.lucene.index.resource;

import junit.framework.TestCase;

import org.apache.lucene.analysis.SimpleAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.RAMDirectory;
import org.springmodules.lucene.index.factory.IndexFactory;
import org.springmodules.lucene.index.factory.SimpleIndexFactory;

public abstract class AbstractDefaultResourceManagerTests extends TestCase {

	protected void doCloseIndexWriter(IndexWriter writer) {
		try {
			if( writer!=null ) {
				writer.close();
			}
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}

	private RAMDirectory createRAMDirectory() {
		RAMDirectory directory = new RAMDirectory();
		
		IndexWriter writer = null;
		try {
			writer = new IndexWriter(directory, new SimpleAnalyzer(), true);
			Document document1 = new Document();
			document1.add(new Field("id", "1", Field.Store.YES, Field.Index.UN_TOKENIZED));
			writer.addDocument(document1);
			Document document2 = new Document();
			document2.add(new Field("id", "2", Field.Store.YES, Field.Index.UN_TOKENIZED));
			writer.addDocument(document2);
		} catch(Exception ex) {
			ex.printStackTrace();
		} finally {
			doCloseIndexWriter(writer);
		}
		
		return directory;
	}

	protected IndexFactory createIndexFactory(RAMDirectory directory) throws Exception {
		SimpleIndexFactory indexFactory = new SimpleIndexFactory();
		indexFactory.setDirectory(directory);
		indexFactory.setAnalyzer(new SimpleAnalyzer());
		return indexFactory;
	}

	protected final IndexFactory createIndexFactory() throws Exception {
		RAMDirectory directory = createRAMDirectory();
		return createIndexFactory(directory);
	}
	
	protected final ResourceManager createResourceManager(IndexFactory indexFactory) {
		DefaultResourceManager resourceManager = new DefaultResourceManager();
		resourceManager.setIndexFactory(indexFactory);
		//resourceManager.afterPropertiesSet();
		return resourceManager;
	}
}
