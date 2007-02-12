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

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.Directory;
import org.springmodules.lucene.index.LuceneIndexAccessException;

/**
 * <p>This is the simplier factory to get reader and writer instances
 * to work on a Lucene index. 
 * 
 * <p>This factory only constructs IndexReader and IndexWriter instances.
 * There is no control on current use of the different methods of the
 * reader and the writer.
 * 
 * <p>Before creating an IndexWriter, this implementation checks if the
 * index already exists. If not, it sets a flag to notify the IndexWriter
 * to create it.
 * 
 * @author Brian McCallister
 * @author Thierry Templier
 * @see org.springmodules.lucene.index.factory.IndexFactory
 * @see org.springmodules.lucene.index.factory.AbstractIndexFactory
 * @see org.springmodules.lucene.index.factory.AbstractIndexFactory#setIndexWriterParameters(IndexWriter)
 * @see org.apache.lucene.index.IndexReader
 * @see org.apache.lucene.index.IndexWriter
 */
public class SimpleIndexFactory extends AbstractIndexFactory implements IndexFactory {

	private boolean resolveLock;
	private boolean create;

	/**
	 * Construct a new SimpleIndexFactory for bean usage.
	 * Note: The Directory and the Analyzer have to be set before using the instance.
	 * @see #setDirectory
	 * @see #setAnalyzer
	 */
	public SimpleIndexFactory() {
	}

	/**
	 * Construct a new SimpleIndexFactory, given a Directory and an Analyzer to
	 * obtain both IndexReader and IndexWriter.
	 * @param directory Lucene directoy which represents an index
	 * @param analyzer Lucene analyzer to construct an IndexWriter
	 */
	public SimpleIndexFactory(Directory directory,Analyzer analyzer) {
		setDirectory(directory);
		setAnalyzer(analyzer);
	}

	/**
	 * Set if the locking must be resolved if the index is locked.
	 */
	public void setResolveLock(boolean resolveLock) {
		this.resolveLock = resolveLock;
	}

	/**
	 * Return if the locking must be resolved if the index is locked.
	 */
	public boolean isResolveLock() {
		return resolveLock;
	}

	/**
	 * Set if the index must be created if it does not exist.
	 */
	public void setCreate(boolean create) {
		this.create = create;
	}

	/**
	 * Return if the index must be created if it does not exist.
	 */
	public boolean isCreate() {
		return create;
	}

	/**
	 * Check if the directory is specified for the factory. If not, the
	 * method will throw a LuceneIndexAccessException exception
	 */
	private void checkDirectory() {
		if( getDirectory()==null ) {
			throw new LuceneIndexAccessException("The directory is not specified");
		}
	}

	/**
	 * Check the index must be unlock if the resolveLock property is
	 * set to true 
	 * @throws IOException if thrown by Lucene methods
	 */
	private void checkIndexLocking() throws IOException {
		boolean locked = IndexReader.isLocked(getDirectory());
		if( locked ) {
			if( resolveLock ) {
				IndexReader.unlock(getDirectory());
			} else {
				throw new LuceneIndexAccessException("The index is locked");
			}
		}
	}

	/**
	 * Contruct a new IndexReader instance based on the directory property. This
	 * instance will be used by the IndexTemplate to get informations about the
	 * index and make delete operations on the index. 
	 * @return a new reader instance on the index
	 * @see org.springmodules.lucene.index.factory.IndexFactory#getIndexReader()
	 */
	public LuceneIndexReader getIndexReader() {
		try {
			checkDirectory();
			checkIndexLocking();

			boolean exist = IndexReader.indexExists(getDirectory());
			if( exist ) {
				return new SimpleLuceneIndexReader(IndexReader.open(getDirectory()));
			} else {
				throw new LuceneIndexAccessException("The index doesn't exist for the specified directory");
			}
		} catch(IOException ex) {
			throw new LuceneIndexAccessException("Error during opening the reader",ex);
		}
	}

	/**
	 * Contruct a new IndexWriter instance based on the directory and analyzer
	 * properties. This instance will be used by both the IndexTemplate and
	 * every indexers to add documents and optimize it.
	 * <p>Before creating an IndexWriter, this implementation checks if the
 	 * index already exists. If not, it sets a flag to notify the IndexWriter
	 * to create it.
	 * @return a new writer instance on the index
	 * @see org.springmodules.lucene.index.factory.IndexFactory#getIndexWriter()
	 * @see org.springmodules.lucene.index.factory.AbstractIndexFactory#setIndexWriterParameters(IndexWriter)
	 * @see IndexReader#indexExists(org.apache.lucene.store.Directory)
	 */
	public LuceneIndexWriter getIndexWriter() {
		try {
			checkDirectory();
			checkIndexLocking();

			boolean exists = IndexReader.indexExists(getDirectory());
			if( !exists && !create ) {
					throw new LuceneIndexAccessException("The index doesn't exist for the specified directory. "+
									"To allow the creation of the index, set the create property to true.");
			}

			IndexWriter writer = new IndexWriter(getDirectory(), getAnalyzer(), !exists);
			setIndexWriterParameters(writer);
			return new SimpleLuceneIndexWriter(writer);
		} catch(IOException ex) {
			throw new LuceneIndexAccessException("Error during creating the writer",ex);
		}
	}

}
