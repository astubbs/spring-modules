/*
 * Copyright 2002-2007 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springmodules.lucene.index.factory;

import java.io.IOException;
import java.io.PrintStream;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.search.Similarity;
import org.apache.lucene.store.Directory;

/**
 * @author Thierry Templier
 */
public class SimpleLuceneIndexWriter implements LuceneIndexWriter {
	private IndexWriter indexWriter;

	public SimpleLuceneIndexWriter(IndexWriter indexWriter) {
		this.indexWriter = indexWriter;
	}
	
	public void addDocument(Document doc, Analyzer analyzer) throws IOException {
		indexWriter.addDocument(doc, analyzer);
	}

	public void addDocument(Document doc) throws IOException {
		indexWriter.addDocument(doc);
	}

	public void addIndexes(Directory[] dirs) throws IOException {
		indexWriter.addIndexes(dirs);
	}

	public void addIndexes(IndexReader[] readers) throws IOException {
		indexWriter.addIndexes(readers);
	}

	public void close() throws IOException {
		indexWriter.close();
	}

	public int docCount() {
		return indexWriter.docCount();
	}

	public boolean equals(Object obj) {
		return indexWriter.equals(obj);
	}

	public Analyzer getAnalyzer() {
		return indexWriter.getAnalyzer();
	}

	public long getCommitLockTimeout() {
		return indexWriter.getCommitLockTimeout();
	}

	public Directory getDirectory() {
		return indexWriter.getDirectory();
	}

	public PrintStream getInfoStream() {
		return indexWriter.getInfoStream();
	}

	public int getMaxBufferedDocs() {
		return indexWriter.getMaxBufferedDocs();
	}

	public int getMaxFieldLength() {
		return indexWriter.getMaxFieldLength();
	}

	public int getMaxMergeDocs() {
		return indexWriter.getMaxMergeDocs();
	}

	public int getMergeFactor() {
		return indexWriter.getMergeFactor();
	}

	public Similarity getSimilarity() {
		return indexWriter.getSimilarity();
	}

	public int getTermIndexInterval() {
		return indexWriter.getTermIndexInterval();
	}

	public boolean getUseCompoundFile() {
		return indexWriter.getUseCompoundFile();
	}

	public long getWriteLockTimeout() {
		return indexWriter.getWriteLockTimeout();
	}

	public int hashCode() {
		return indexWriter.hashCode();
	}

	public void optimize() throws IOException {
		indexWriter.optimize();
	}

	public void setInfoStream(PrintStream infoStream) {
		indexWriter.setInfoStream(infoStream);
	}

	public void setMaxBufferedDocs(int maxBufferedDocs) {
		indexWriter.setMaxBufferedDocs(maxBufferedDocs);
	}

	public void setMaxFieldLength(int maxFieldLength) {
		indexWriter.setMaxFieldLength(maxFieldLength);
	}

	public void setMaxMergeDocs(int maxMergeDocs) {
		indexWriter.setMaxMergeDocs(maxMergeDocs);
	}

	public void setMergeFactor(int mergeFactor) {
		indexWriter.setMergeFactor(mergeFactor);
	}

	public void setSimilarity(Similarity similarity) {
		indexWriter.setSimilarity(similarity);
	}

	public void setTermIndexInterval(int interval) {
		indexWriter.setTermIndexInterval(interval);
	}

	public void setUseCompoundFile(boolean value) {
		indexWriter.setUseCompoundFile(value);
	}

	public void setWriteLockTimeout(long writeLockTimeout) {
		indexWriter.setWriteLockTimeout(writeLockTimeout);
	}

	public void setCommitLockTimeout(long commitLockTimeout) {
		indexWriter.setCommitLockTimeout(commitLockTimeout);
	}
}
