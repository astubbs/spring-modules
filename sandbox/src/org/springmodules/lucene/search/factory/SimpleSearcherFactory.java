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

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Searcher;
import org.apache.lucene.store.Directory;
import org.springmodules.lucene.index.factory.IndexFactory;
import org.springmodules.lucene.index.factory.IndexReaderFactoryUtils;
import org.springmodules.lucene.search.LuceneSearchException;

/**
 * This is the simplier factory to get searcher instances to search
 * informations in a single Lucene index. 
 * 
 * @author Thierry Templier
 * @see org.springmodules.lucene.search.factory.SearcherFactory
 */
public class SimpleSearcherFactory extends AbstractSingleSearcherFactory implements SearcherFactory {

	/**
	 * Construct a new SimpleSearcherFactory for bean usage.
	 * Note: The Directory has to be set before using the instance.
	 * This constructor can be used to prepare a SimpleSearcherFactory via a BeanFactory,
	 * typically setting the Directory via setDirectory.
	 * @see AbstractSingleSearcherFactory#setDirectory(Directory)
	 */
	public SimpleSearcherFactory() {
	}

	/**
	 * Construct a new SimpleSearcherFactory, given an Directory to obtain
	 * a Searcher.
	 * @param directory Directory to obtain Searcher
	 */
	public SimpleSearcherFactory(Directory directory) {
		setDirectory(directory);
	}

	/**
	 * Construct a new SimpleSearcherFactory, given an IndexFactory to obtain
	 * a Searcher.
	 * @param indexFactory IndexFactory to obtain Searcher
	 */
	public SimpleSearcherFactory(IndexFactory indexFactory) {
		setIndexFactory(indexFactory);
	}

	/**
	 * This method creates a new intance of a Searcher on the configured
	 * index (from a Directory or an IndexReader).
	 * 
	 * @return a Searcher instance
	 * @see org.springmodules.lucene.search.SearcherFactory#getSearcher()
	 */
	public Searcher getSearcher() throws IOException {
		if( getDirectory()!=null ) {
			return new IndexSearcher(getDirectory());
		} else if( getIndexFactory()!=null ) {
			IndexReader indexReader=IndexReaderFactoryUtils.getIndexReader(getIndexFactory());
			return new IndexSearcher(indexReader);
		} else {
			throw new LuceneSearchException("Either a Directory or an Indexreader must be specified.");
		}
	}

}
