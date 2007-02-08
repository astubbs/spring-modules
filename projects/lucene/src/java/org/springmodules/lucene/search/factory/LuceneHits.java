package org.springmodules.lucene.search.factory;

import java.io.IOException;
import java.util.Iterator;

import org.apache.lucene.document.Document;

public interface LuceneHits {
    int length();
    Document doc(int n) throws IOException;
	float score(int n) throws IOException;
	int id(int n) throws IOException;
	Iterator iterator();
}
