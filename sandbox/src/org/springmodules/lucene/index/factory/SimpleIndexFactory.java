/*
 * Copyright 2002-2005 the original author or authors.
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

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.Directory;
import org.springmodules.lucene.index.LuceneOpenIndexException;

/**
 * Template for index manipulation.
 * 
 * @author Brian McCallister
 * @author Thierry Templier
 */
public class SimpleIndexFactory extends AbstractIndexFactory implements IndexFactory {

	private Directory directory;
	private Analyzer analyzer;

	public SimpleIndexFactory(Directory directory,Analyzer analyzer) {
		this.directory=directory;
		this.analyzer=analyzer;
	}

	/**
	 * @see org.springmodules.lucene.index.factory.IndexFactory#getIndexReader()
	 */
	public IndexReader getIndexReader() {
		try {
			return IndexReader.open(directory);
		} catch(IOException ex) {
			throw new LuceneOpenIndexException("Error during opening the reader",ex);
		}
	}

	/**
	 * @see org.springmodules.lucene.index.factory.IndexFactory#getIndexWriter()
	 */
	public IndexWriter getIndexWriter() {
		try {
			boolean create = !IndexReader.indexExists(directory);
			IndexWriter writer = new IndexWriter(directory,analyzer,create);
			setIndexWriterParameters(writer);
			return writer;
		} catch(IOException ex) {
			throw new LuceneOpenIndexException("Error during creating the writer",ex);
		}
	}

	/**
	 * @return
	 */
	public Directory getDirectory() {
		return directory;
	}

	/**
	 * @param directory
	 */
	public void setDirectory(Directory directory) {
		this.directory = directory;
	}

	/**
	 * @return
	 */
	public Analyzer getAnalyzer() {
		return analyzer;
	}

	/**
	 * @param analyzer
	 */
	public void setAnalyzer(Analyzer analyzer) {
		this.analyzer = analyzer;
	}

}
