package org.springmodules.lucene.index.factory.concurrent;

import java.io.IOException;

import org.apache.lucene.analysis.SimpleAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.RAMDirectory;

import junit.framework.TestCase;

public class ConcurrencyTests extends TestCase {
	private RAMDirectory directory;
	
	protected void initializeIndex(IndexWriter writer) throws IOException {
		Document document1 = new Document();
		document1.add(new Field("id", "1", Field.Store.YES, Field.Index.UN_TOKENIZED));
		document1.add(new Field("field", "a sample", Field.Store.YES, Field.Index.TOKENIZED));
		document1.add(new Field("filter", "a sample filter", Field.Store.YES, Field.Index.TOKENIZED));
		document1.add(new Field("sort", "2", Field.Store.YES, Field.Index.UN_TOKENIZED));
		writer.addDocument(document1);

		Document document2 = new Document();
		document2.add(new Field("id", "2", Field.Store.YES, Field.Index.UN_TOKENIZED));
		document2.add(new Field("field", "a sample", Field.Store.YES, Field.Index.TOKENIZED));
		document2.add(new Field("filter", "a sample filter", Field.Store.YES, Field.Index.TOKENIZED));
		document2.add(new Field("test", "value", Field.Store.YES, Field.Index.TOKENIZED));
		document2.add(new Field("sort", "2", Field.Store.YES, Field.Index.UN_TOKENIZED));
		writer.addDocument(document2);
	}
	
	protected void doCloseIndexWriter(IndexWriter writer) {
		try {
			if( writer!=null ) {
				writer.close();
			}
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}

	protected void setUp() throws Exception {
		System.out.println("---- initIndex -----");
		directory = new RAMDirectory();
		
		IndexWriter writer = null;
		try {
			writer = new IndexWriter(directory, new SimpleAnalyzer(), true);
			initializeIndex(writer);
		} catch(Exception ex) {
			ex.printStackTrace();
		} finally {
			doCloseIndexWriter(writer);
		}
	}

	protected void tearDown() throws Exception {
		directory.close();
		directory = null;
	}
	
	public void testOpenTwoIndexReader() throws Exception {
		IndexReader reader1 = IndexReader.open(directory);
		IndexReader reader2 = IndexReader.open(directory);

		reader1.close();
		reader2.close();
	}

	public void testOpenTwoIndexWriter() throws Exception {
		try {
			IndexWriter writer1 = new IndexWriter(directory, new SimpleAnalyzer(), false);
			IndexWriter writer2 = new IndexWriter(directory, new SimpleAnalyzer(), false);
			fail();
		} catch(IOException ex) { }
	}

	public void testOpenIndexReaderWriter() throws Exception {
		IndexReader reader = IndexReader.open(directory);
		IndexWriter writer = new IndexWriter(directory, new SimpleAnalyzer(), false);
		
		reader.close();
		writer.close();
	}

	public void testAddDeleteDocumentError1() throws Exception {
		try {
			IndexReader reader = IndexReader.open(directory);
			IndexWriter writer = new IndexWriter(directory, new SimpleAnalyzer(), false);
		
			Document document = new Document();
			document.add(new Field("id", "1", Field.Store.YES, Field.Index.UN_TOKENIZED));
			document.add(new Field("field", "a sample", Field.Store.YES, Field.Index.TOKENIZED));
			document.add(new Field("filter", "a sample filter", Field.Store.YES, Field.Index.TOKENIZED));
			document.add(new Field("sort", "2", Field.Store.YES, Field.Index.UN_TOKENIZED));
			writer.addDocument(document);

			reader.deleteDocuments(new Term("field", "sample"));
		
			fail();
		} catch(IOException ex) {}
	}

	public void testAddDeleteDocumentError2() throws Exception {
		try {
			IndexReader reader = IndexReader.open(directory);
			IndexWriter writer = new IndexWriter(directory, new SimpleAnalyzer(), false);
		
			Document document = new Document();
			document.add(new Field("id", "1", Field.Store.YES, Field.Index.UN_TOKENIZED));
			document.add(new Field("field", "a sample", Field.Store.YES, Field.Index.TOKENIZED));
			document.add(new Field("filter", "a sample filter", Field.Store.YES, Field.Index.TOKENIZED));
			document.add(new Field("sort", "2", Field.Store.YES, Field.Index.UN_TOKENIZED));
			writer.addDocument(document);
			writer.close();

			reader.deleteDocuments(new Term("field", "sample"));
			reader.close();
		
			fail();
		} catch(IOException ex) {}
	}

	public void testAddDeleteDocumentSuccess() throws Exception {
		IndexWriter writer = new IndexWriter(directory, new SimpleAnalyzer(), false);
		Document document = new Document();
		document.add(new Field("id", "1", Field.Store.YES, Field.Index.UN_TOKENIZED));
		document.add(new Field("field", "a sample", Field.Store.YES, Field.Index.TOKENIZED));
		document.add(new Field("filter", "a sample filter", Field.Store.YES, Field.Index.TOKENIZED));
		document.add(new Field("sort", "2", Field.Store.YES, Field.Index.UN_TOKENIZED));
		writer.addDocument(document);
		writer.close();

		IndexReader reader = IndexReader.open(directory);
		reader.deleteDocuments(new Term("field", "sample"));
		reader.close();
	}

	public void testAddOptimizeDocument() throws Exception {
		IndexWriter writer = new IndexWriter(directory, new SimpleAnalyzer(), false);
		Document document = new Document();
		document.add(new Field("id", "1", Field.Store.YES, Field.Index.UN_TOKENIZED));
		document.add(new Field("field", "a sample", Field.Store.YES, Field.Index.TOKENIZED));
		document.add(new Field("filter", "a sample filter", Field.Store.YES, Field.Index.TOKENIZED));
		document.add(new Field("sort", "2", Field.Store.YES, Field.Index.UN_TOKENIZED));
		writer.addDocument(document);

		writer.optimize();
		
		writer.close();
	}

	public void testDeleteOptimizeDocumentError1() throws Exception {
		try {
			IndexWriter writer = new IndexWriter(directory, new SimpleAnalyzer(), false);
			IndexReader reader = IndexReader.open(directory);
			reader.deleteDocuments(new Term("field", "sample"));
			writer.optimize();
			
			fail();
		} catch (Exception ex) { }
	}

	public void testDeleteOptimizeDocumentError2() throws Exception {
		try {
			IndexWriter writer = new IndexWriter(directory, new SimpleAnalyzer(), false);
			IndexReader reader = IndexReader.open(directory);
			reader.deleteDocuments(new Term("field", "sample"));
			reader.close();
			writer.optimize();
			writer.close();
			
			fail();
		} catch (Exception ex) { }
	}

	public void testDeleteOptimizeDocumentSucess() throws Exception {
		IndexReader reader = IndexReader.open(directory);
		reader.deleteDocuments(new Term("field", "sample"));
		reader.close();

		IndexWriter writer = new IndexWriter(directory, new SimpleAnalyzer(), false);
		writer.optimize();
		writer.close();
	}
}
