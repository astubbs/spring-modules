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

package org.springmodules.lucene.index.support;

import org.apache.lucene.analysis.Analyzer;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.InitializingBean;
import org.springmodules.lucene.index.core.DefaultLuceneIndexTemplate;
import org.springmodules.lucene.index.core.LuceneIndexTemplate;
import org.springmodules.lucene.index.factory.IndexFactory;
import org.springmodules.lucene.index.support.file.DocumentHandlerManager;

/**
 * Convenient super class for objects using Lucene indexing
 * using a LuceneIndexTemplate instance.
 * Requires a IndexFactory to be set if the template is not
 * injected directly and providing a LuceneIndexTemplate based
 * on it to subclasses.
 *
 * <p>This base class is mainly intended for LuceneIndexTemplate usage
 * but can also be used when working with IndexWriterFactoryUtils and
 * IndexReaderFactoryUtils directly or with indexer classes.
 *
 * <p>By default, a DefaultLuceneIndexTemplate instance is created. If
 * you need another implementation of the LuceneIndexTemplate interface,
 * you can directly inject it the <code>template</code> property. For
 * example, you can inject the ConcurrentLuceneIndexTemplate to manage
 * concurrent calls on the template. 
 *
 * <p>The DocumentHandlerManager to use can be too specify to
 * allow different indexing types.
 *
 * @author Thierry Templier
 * @see org.springmodules.lucene.index.core.DefaultLuceneIndexTemplate
 * @see org.springmodules.lucene.index.core.concurrent.ConcurrentLuceneIndexTemplate
 * @see org.springmodules.lucene.index.core.concurrent.ConcurrentLuceneIndexTemplateListener
 */
public abstract class LuceneIndexSupport implements InitializingBean {
	private LuceneIndexTemplate template;
	private IndexFactory indexFactory;
	private Analyzer analyzer;
	private DocumentHandlerManager documentHandlerManager;

	/**
	 * Set the IndexFactory to obtain both IndexReader and IndexWriter.
	 */
	public void setIndexFactory(IndexFactory factory) {
		indexFactory = factory;
	}

	/**
	 * Return the IndexFactory used by this template.
	 */
	public IndexFactory getIndexFactory() {
		return indexFactory;
	}

	/**
	 * Set the default Lucene Analyzer used to extract tokens out of the
	 * text to index.
	 */
	public void setAnalyzer(Analyzer analyzer) {
		this.analyzer = analyzer;
	}

	/**
	 * Return the Lucene Analyzer used by this template.
	 */
	public Analyzer getAnalyzer() {
		return analyzer;
	}

	/**
	 * Set a configured template.
	 */
	public void setTemplate(LuceneIndexTemplate template) {
		this.template = template;
	}

	/**
	 * Return the LuceneIndexTemplate to use.
	 */
	public LuceneIndexTemplate getTemplate() {
		return template;
	}

	/**
	 * Set the DocumentHandlerManager to allow different indexing types
	 */
	public void setDocumentHandlerManager(DocumentHandlerManager documentHandlerManager) {
		this.documentHandlerManager = documentHandlerManager;
	}

	/**
	 * Return the DocumentHandlerManager to allow different indexing types
	 */
	public DocumentHandlerManager getDocumentHandlerManager() {
		return documentHandlerManager;
	}

	/**
	 * This method constructs a LuceneIndexTemplate basing indexFactory
	 * and analyzer properties if it is not injected. 
	 */
	public void afterPropertiesSet() throws Exception {
		if( this.template==null && this.indexFactory==null ) {
			throw new BeanInitializationException("indexFactory property required");
		}

		if( this.template==null ) {
			this.template=new DefaultLuceneIndexTemplate(indexFactory,analyzer);
		}
	}

}
