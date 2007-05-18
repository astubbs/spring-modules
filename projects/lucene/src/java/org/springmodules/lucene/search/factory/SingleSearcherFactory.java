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

package org.springmodules.lucene.search.factory;

import java.io.IOException;

import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Searcher;
import org.apache.lucene.store.Directory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springmodules.lucene.index.factory.IndexFactory;
import org.springmodules.lucene.index.factory.IndexReaderFactoryUtils;
import org.springmodules.lucene.index.factory.LuceneIndexReader;
import org.springmodules.lucene.search.LuceneSearchException;

/**
 * This is the factory to use a single instance of a searcher to
 * make searchs in a single Lucene index. 
 * 
 * @author Thierry Templier
 * @see org.springmodules.lucene.search.factory.SearcherFactory
 */
public class SingleSearcherFactory extends AbstractSingleSearcherFactory implements InitializingBean, DisposableBean {

	private LuceneSearcher indexSearcher;

	/**
	 * Construct a new SingleSearcherFactory for bean usage.
	 * Note: The Directory has to be set before using the instance.
	 * This constructor can be used to prepare a SimpleSearcherFactory
	 * via a BeanFactory, typically setting the Directory via
	 * setDirectory or the IndexFactory via setIndexFactory.
	 * @see AbstractSingleSearcherFactory#setDirectory(Directory)
	 * @see AbstractSingleSearcherFactory#setIndexFactory(IndexFactory)
	 */
	public SingleSearcherFactory() {
	}

	/**
	 * Construct a new SingleSearcherFactory, given an Directory to obtain
	 * a Searcher.
	 * @param directory Directory to obtain Searcher
	 */
	public SingleSearcherFactory(Directory directory) {
		setDirectory(directory);
	}

	/**
	 * Construct a new SingleSearcherFactory, given an IndexFactory to
	 * obtain a Searcher.
	 * @param indexFactory IndexFactory to obtain Searcher
	 */
	public SingleSearcherFactory(IndexFactory indexFactory) {
		setIndexFactory(indexFactory);
	}

	/**
	 * This method initializes the single searcher instance.
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	public void afterPropertiesSet() throws Exception {
		if( getDirectory()!=null ) {
			Searcher searcher = new IndexSearcher(getDirectory());
			this.indexSearcher = new SimpleLuceneSearcher(searcher);
		} else if( getIndexFactory()!=null ) {
			LuceneIndexReader indexReader = IndexReaderFactoryUtils.getIndexReader(getIndexFactory());
			Searcher searcher = indexReader.createNativeSearcher();
			this.indexSearcher = new SimpleLuceneSearcher(searcher);
		} else {
			throw new LuceneSearchException("Either a Directory or an IndexReader must be specified.");
		}
	}

	/**
	 * This method closes the single searcher instance
	 * @see org.springframework.beans.factory.DisposableBean#destroy()
	 */
	public void destroy() throws Exception {
		SearcherFactoryUtils.closeSearcher(this.indexSearcher);
	}

	/**
	 * This method creates a new intance of a Searcher on the configured
	 * index (from a Directory or an IndexReader).
	 * 
	 * @return a Searcher instance
	 * @see org.springmodules.lucene.search.SearcherFactory#getSearcher()
	 */
	public LuceneSearcher getSearcher() throws IOException {
		return indexSearcher;
	}

}
