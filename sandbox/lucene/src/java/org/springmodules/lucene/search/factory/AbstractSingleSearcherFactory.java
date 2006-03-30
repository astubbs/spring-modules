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

/**
 * Abstract class of every single searcher factories to
 * help to configure and search a single index.
 * 
 * @author Thierry Templier
 */
public abstract class AbstractSingleSearcherFactory {

	private Directory directory;
	private IndexFactory indexFactory;

	/**
	 * Return the Lucene Directory used by index factory.
	 */
	public Directory getDirectory() {
		return directory;
	}

	/**
	 * Set the Lucene Directory to be used.
	 */
	public void setDirectory(Directory directory) {
		this.directory = directory;
	}

	/**
	 * Return the Lucene IndexFactory used by index factory.
	 */
	public IndexFactory getIndexFactory() {
		return indexFactory;
	}

	/**
	 * Set the Lucene IndexFactory to be used.
	 */
	public void setIndexFactory(IndexFactory indexFactory) {
		this.indexFactory = indexFactory;
	}

}
