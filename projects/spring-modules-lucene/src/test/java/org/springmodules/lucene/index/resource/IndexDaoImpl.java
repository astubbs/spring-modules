package org.springmodules.lucene.index.resource;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.Term;
import org.springmodules.lucene.index.support.LuceneIndexSupport;

public class IndexDaoImpl extends LuceneIndexSupport implements IndexDao {

	public void myMethod1() {
		Document document = new Document();
		document.add(new Field("id", "3", Field.Store.YES, Field.Index.UN_TOKENIZED));
		getLuceneIndexTemplate().addDocument(document);
		getLuceneIndexTemplate().addDocument(document);
		getLuceneIndexTemplate().addDocument(document);
	}

	public void myMethod2() {
		Document document = new Document();
		document.add(new Field("id", "3", Field.Store.YES, Field.Index.UN_TOKENIZED));
		getLuceneIndexTemplate().addDocument(document);
		getLuceneIndexTemplate().addDocument(document);
		getLuceneIndexTemplate().addDocument(document);
		getLuceneIndexTemplate().deleteDocuments(new Term("id", "3"));
	}

	public void myMethod3() {
		getLuceneIndexTemplate().deleteDocuments(new Term("id", "3"));
		getLuceneIndexTemplate().deleteDocuments(new Term("id", "3"));
		getLuceneIndexTemplate().deleteDocuments(new Term("id", "3"));
	}

	public void myMethod4() {
		getLuceneIndexTemplate().deleteDocuments(new Term("id", "3"));
		Document document = new Document();
		document.add(new Field("id", "3", Field.Store.YES, Field.Index.UN_TOKENIZED));
		getLuceneIndexTemplate().addDocument(document);
		getLuceneIndexTemplate().addDocument(document);
		getLuceneIndexTemplate().addDocument(document);
	}
}
