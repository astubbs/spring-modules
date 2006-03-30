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

package org.springmodules.lucene.search.factory;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Searcher;
import org.apache.lucene.store.Directory;
import org.springmodules.lucene.index.factory.IndexFactory;
import org.springmodules.lucene.index.factory.IndexReaderFactoryUtils;
import org.springmodules.lucene.search.LuceneSearchException;

/**
 * Abstract class of every multiple searcher factories to
 * help to configure and search multiple indexes.
 * 
 * @author Thierry Templier
 */
public abstract class AbstractMultipleSearcherFactory {

	private Directory[] directories;
	private IndexFactory[] indexFactories;

	/**
	 * Return the Lucene Directories used by index factories.
	 */
	public Directory[] getDirectories() {
		return directories;
	}

	/**
	 * Set the Lucene Directories to be used.
	 */
	public void setDirectories(Directory[] directories) {
		this.directories = directories;
	}

	/**
	 * Set the Lucene IndexFactory to be used.
	 */
	public void setIndexFactories(IndexFactory[] indexFactories) {
		this.indexFactories = indexFactories;
	}

	/**
	 * Return the Lucene IndexFactories used by index factories.
	 */
	public IndexFactory[] getIndexFactories() {
		return indexFactories;
	}

	/**
	 * This method creates all the searchers for every configured
	 * Lucene directories to search (from Directories or/and IndexReaders).
	 * @return the searchers on every directories
	 * @throws IOException if thrown by Lucene methods
	 */
	protected Searcher[] createSearchers() throws IOException {
		if( (directories==null || directories.length==0)
		       && (indexFactories==null || indexFactories.length==0) ) {
			throw new LuceneSearchException("Either Directories or Indexreaders must be specified.");
		}

		int size=0;
		if( directories!=null ) {
			size=directories.length;
		}
		if( indexFactories!=null ) {
			size+=indexFactories.length;
		}
		Searcher[] searchers=new Searcher[size];

		if( directories!=null ) {
			for(int index=0;index<directories.length;index++) {
				searchers[index]=new IndexSearcher(directories[index]);
			}
		}
		if( indexFactories!=null ) {
			int startSearchersIndex=0;
			if( directories!=null ) {
				startSearchersIndex=directories.length;
			}
			for(int index=0;index<indexFactories.length;index++) {
				IndexReader indexReader=IndexReaderFactoryUtils.getIndexReader(indexFactories[index]);
				searchers[index+startSearchersIndex]=new IndexSearcher(indexReader);
			}
		}
		return searchers;
	}

}
