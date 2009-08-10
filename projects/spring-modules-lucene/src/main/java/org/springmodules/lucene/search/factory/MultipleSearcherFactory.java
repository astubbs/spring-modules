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

import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.MultiSearcher;
import org.apache.lucene.search.Searcher;
import org.apache.lucene.store.Directory;
import org.springmodules.lucene.index.factory.IndexFactory;

/**
 * This is the multiple index factory to get searcher instances to search
 * informations in several Lucene indexes. 
 * 
 * @author Thierry Templier
 * @see org.springmodules.lucene.search.factory.SearcherFactory
 */
public class MultipleSearcherFactory extends AbstractMultipleSearcherFactory implements SearcherFactory {

	/**
	 * Construct a new MultipleSearcherFactory for bean usage.
	 * Note: The Directories have to be set before using the instance.
	 * This constructor can be used to prepare a MultipleSearcherFactory via a BeanFactory,
	 * typically setting the Directory via setDirectories.
	 * @see AbstractMultipleSearcherFactory#setDirectories(List)
	 */
	public MultipleSearcherFactory() {
	}

	/**
	 * Construct a new MultipleSearcherFactory, given Directories to obtain
	 * a Searcher.
	 * @param directories Directories to obtain Searcher
	 */
	public MultipleSearcherFactory(Directory[] directories) {
		setDirectories(directories);
	}

	/**
	 * Construct a new MultipleSearcherFactory, given IndexFactories to obtain
	 * a Searcher.
	 * @param indexFactories IndexFactories to obtain Searcher
	 */
	public MultipleSearcherFactory(IndexFactory[] indexFactories) {
		setIndexFactories(indexFactories);
	}

	/**
	 * Construct a new MultipleSearcherFactory, given Directories and
	 * IndexFactories to obtain a Searcher.
	 * @param directories Directories to obtain Searcher
	 * @param indexFactories IndexFactories to obtain Searcher
	 */
	public MultipleSearcherFactory(Directory[] directories,IndexFactory[] indexFactories) {
		setDirectories(directories);
		setIndexFactories(indexFactories);
	}

	/**
	 * This method creates a new instance of a Searcher on the configured
	 * indexes.
	 * 
	 * @return a Searcher instance
	 * @see org.springmodules.lucene.search.SearcherFactory#getSearcher()
	 */
	public LuceneSearcher getSearcher() throws IOException {
		Searcher[] searchers = createSearchers();
		MultiSearcher multiSearcher = new MultiSearcher(searchers); 
		return new SimpleLuceneSearcher(multiSearcher);
	}

}
